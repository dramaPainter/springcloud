package drama.painter.web.mall.service.pay.provider.exchange;

import com.fasterxml.jackson.annotation.JsonProperty;
import drama.painter.core.web.utility.Randoms;
import drama.painter.web.mall.service.pay.anomaly.CallbackException;
import drama.painter.web.mall.service.pay.anomaly.OrderProccessedException;
import drama.painter.web.mall.service.pay.enums.EncryptEnum;
import drama.painter.web.mall.service.pay.enums.SerializeEnum;
import drama.painter.web.mall.service.pay.interfaces.ICallback;
import drama.painter.web.mall.service.pay.interfaces.IMessage;
import drama.painter.web.mall.service.pay.interfaces.IService;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author murphy
 * 集团支付宝兑换
 */
public class GroupAlipayImpl implements IService {
	private static final Map<String, Object> PARAM = new LinkedHashMap();
	private static final ICallback CALLBACK = new GroupAlipayCallback();

	static {
		PARAM.put("order_id", "<orderid>");
		PARAM.put("type", 0);
		PARAM.put("amount", "<cash%sy>");
		PARAM.put("fullname", "<account>");
		PARAM.put("account", "<realname>");
		PARAM.put("callback_url", "<callbackUrl>");
		PARAM.put("device_type", "android");
		PARAM.put("device_id", Randoms.getRandom(9));
		PARAM.put("device_ip", "<ip>");
		PARAM.put("player_id", "<userid%s>");
	}

	@Override
	public Integer id() {
		return 5;
	}

	@Override
	public Map<String, Object> param() {
		return PARAM;
	}

	@Override
	public EncryptEnum encrypter() {
		return EncryptEnum.MD5_TEXT;
	}

	@Override
	public SerializeEnum serializer() {
		return SerializeEnum.JSON;
	}

	@Override
	public Boolean encoding() {
		return false;
	}

	@Override
	public EncryptEnum verifier() {
		return EncryptEnum.MD5_POST_TEXT;
	}

	@Override
	public String extract(String text) throws OrderProccessedException, CallbackException {
		boolean repeat = "请不要重复提交请求".equals(text);
		if (repeat) {
			throw new OrderProccessedException(text);
		} else if (!IMessage.SUCCESS.equals(text)) {
			throw new CallbackException(text);
		} else {
			return null;
		}
	}

	@Override
	public ICallback callback() {
		return CALLBACK;
	}

	@Override
	public String reply() {
		return "success";
	}

	@Data
	public static class GroupAlipayCallback implements ICallback {
		@JsonProperty("order_id")
		private long orderId;
		@JsonProperty("trade_no")
		private String tradeNo;
		private String msg;
		private int code;
		@JsonProperty("order_amout")
		private float orderAmount;
		@JsonProperty("pay_time")
		private String payTime;

		@Override
		public String getCallDate() {
			return payTime;
		}

		@Override
		public Long getCallOrderId() {
			return orderId;
		}

		@Override
		public String getCallFlowId() {
			return tradeNo;
		}

		@Override
		public int getCallCash() {
			return (int) (orderAmount * 100);
		}

		@Override
		public boolean succeed() {
			return code == 10000;
		}

		@Override
		public String getCallMessage() {
			return msg;
		}
	}
}



