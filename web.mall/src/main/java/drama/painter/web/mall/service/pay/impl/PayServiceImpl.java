package drama.painter.web.mall.service.pay.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import drama.painter.core.web.tool.ClassLoader;
import drama.painter.core.web.tool.HttpLog;
import drama.painter.core.web.tool.Json;
import drama.painter.core.web.utility.Networks;
import drama.painter.web.mall.service.pay.anomaly.CallbackException;
import drama.painter.web.mall.service.pay.enums.SerializeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import drama.painter.web.mall.service.pay.anomaly.ServiceNotFoundException;
import drama.painter.web.mall.service.pay.enums.EncryptEnum;
import drama.painter.web.mall.service.pay.enums.ExchangeStatusEnum;
import drama.painter.web.mall.service.pay.interfaces.ICallback;
import drama.painter.web.mall.service.pay.interfaces.IPayService;
import drama.painter.web.mall.service.pay.interfaces.IPersistentService;
import drama.painter.web.mall.service.pay.interfaces.IService;
import drama.painter.web.mall.service.pay.model.Exchange;
import drama.painter.web.mall.service.pay.tool.SignTool;
import drama.painter.web.mall.service.pay.model.ExchangeConfig;
import drama.painter.web.mall.service.pay.provider.exchange.GroupAlipayImpl;

/**
 * @author murphy
 * 充值服务
 */
@Slf4j
@Service
public class PayServiceImpl implements IPayService {
	private static final Map<Integer, IService> MAP = new HashMap();

	@Autowired
	ExchangeConfig config;

	@Autowired
	IPersistentService persistent;

	@Override
	public String exchange(Exchange order, String callbackUrl) {
		String time = String.valueOf(HttpLog.getTime());
		IService service = MAP.get(order.getPartner());
		ExchangeConfig.App app = config.getServiceProvider().get(service.id());
		String text = SignTool.getSerializer(service.serializer()).apply(service.param(), service.encoding());
		text = SignTool.getSubstitute(service.serializer()).apply(text, order, app.getAppId(), callbackUrl);
		String unsign = app.getSign().replaceFirst("<text>", text).replaceFirst("<time>", time);
		String sign = SignTool.getEncrypter(service.encrypter()).apply(unsign);

		try {
			String result = post(text, app.getUrl(), sign, time, service.serializer());
			service.extract(result);
			return "兑换订单已提交，请耐心等待处理。";
		} catch (Exception e) {
			log.error("订单[{}]发起兑换出错", order.getOrderid(), e);
			return "兑换发生错误，请稍后再试。";
		}
	}

	@Override
	public String exchangeBack(int partner, String post, Map<String, String[]> query) {
		IService service = MAP.get(partner);
		if (service == null) {
			String msg = "回调兑换平台不存在:".concat(String.valueOf(partner));
			throw new ServiceNotFoundException(msg);
		}

		ICallback result;
		try {
			result = Json.parseObject(post, service.callback().getClass());
			if (result == null) {
				throw new CallbackException("POST值反序化为NULL");
			}
		} catch (Exception e) {
			throw new CallbackException("POST值反序化失败:".concat(post));
		}

		boolean authorized = verifySign(config.getServiceProvider().get(partner), partner, service.verifier(), post, query);
		ExchangeStatusEnum status = result.succeed() && authorized ? ExchangeStatusEnum.SUCCESS : ExchangeStatusEnum.REWIND;
		Exchange order = Exchange.builder()
			.status(status)
			.cash(result.getCallCash())
			.callback(result.getCallMessage())
			.orderid(result.getCallOrderId())
			.thirdid(result.getCallFlowId())
			.echodate(result.getCallDate())
			.build();
		persistent.update(order);
		return authorized ? service.reply() : "签名不正确：".concat(post);
	}

	private boolean verifySign(ExchangeConfig.App app, int partner, EncryptEnum verifier, String post, Map<String, String[]> query) {
		return true;
	}

	private String post(String text, String url, String sign, String time, SerializeEnum resolver) throws IOException {
		boolean hasSign = url.contains("<sign>");
		if (hasSign) {
			url = url.replaceFirst("<time>", time).replaceFirst("<sign>", sign);
		} else {
			if (resolver == SerializeEnum.JSON) {
				text = text.substring(0, text.length() - 2).concat(String.format(",\"sign\":\"%s\"}", sign));
			} else {
				text = text.concat("&sign=").concat(sign);
			}
		}

		return Networks.postJson(url, text);
	}

	static {
		String path = GroupAlipayImpl.class.getName();
		path = path.substring(0, path.lastIndexOf("."));
		Set<Class<?>> list = ClassLoader.getClassInstance(path);
		try {
			for (Class<?> pay : list) {
				IService service = (IService) pay.newInstance();
				MAP.put(service.id(), service);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		log.info("加载第三方兑换平台{}个", list.size());
		list.clear();
	}
}
