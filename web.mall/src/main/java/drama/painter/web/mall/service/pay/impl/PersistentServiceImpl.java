package drama.painter.web.mall.service.pay.impl;

import drama.painter.web.mall.service.pay.anomaly.OrderNotFoundException;
import drama.painter.web.mall.service.pay.anomaly.OrderProccessedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import drama.painter.web.mall.service.pay.enums.ExchangeStatusEnum;
import drama.painter.web.mall.service.pay.interfaces.IPersistentService;
import drama.painter.web.mall.service.pay.mapper.PlatformMapper;
import drama.painter.web.mall.service.pay.mapper.ZeroMapper;
import drama.painter.web.mall.service.pay.model.Exchange;
import drama.painter.web.mall.service.pay.tool.ExchangeHelper;

/**
 * @author murphy
 * 充值服务
 */
@Slf4j
@Service
public class PersistentServiceImpl implements IPersistentService {
	@Autowired
	ZeroMapper zeroMapper;

	@Autowired
	PlatformMapper platformMapper;

	@Autowired
	NotifyImpl notify;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void update(Exchange order) {
		Exchange now = zeroMapper.getExchange(order.getOrderid());
		if (now == null) {
			throw new OrderNotFoundException(String.format("回调订单[%d]不存在", order.getOrderid()));
		} else if (!now.getCash().equals(order.getCash())) {
			throw new OrderNotFoundException(String.format("回调订单[%d]价格不一致：[原始值：%d, 回调值：%d]", order.getOrderid(), now.getCash(), order.getCash()));
		} else if (now.getStatus().getValue() > ExchangeStatusEnum.READY.getValue()) {
			throw new OrderProccessedException(String.format("回调订单[%d]已经处理过了 ", order.getOrderid()));
		}

		String call = order.getCallback();
		now.setCallback(call == null ? "" : (call.length() > 49 ? call.substring(0, 49) : call));
		now.setEchodate(order.getEchodate());
		now.setStatus(order.getStatus());
		now.setThirdid(order.getThirdid());
		zeroMapper.applyExchange(order.getOrderid(), order.getEchodate(), order.getStatus());
		ExchangeHelper.execute(now, zeroMapper,platformMapper, notify);
	}
}
