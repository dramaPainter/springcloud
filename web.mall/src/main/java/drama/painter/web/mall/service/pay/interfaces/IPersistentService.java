package drama.painter.web.mall.service.pay.interfaces;

import drama.painter.web.mall.service.pay.model.Exchange;

/**
 * @author murphy
 */
public interface IPersistentService {
	/**
	 * 更新兑换订单状态和回调，并做相应的处理
	 * @param order 兑换单对象
	 */
	void update(Exchange order);
}
