package drama.painter.web.mall.service.pay.interfaces;

import drama.painter.web.mall.service.pay.model.Exchange;

import java.util.Map;

/**
 * @author murphy
 */
public interface IPayService {
	/**
	 * 兑换订单
	 *
	 * @param order       兑换单对象
	 * @param callbackUrl 回调网址
	 * @return
	 */
	String exchange(Exchange order, String callbackUrl);

	/**
	 * 回调兑换订单
	 *
	 * @param partner 第三方兑换商户ID
	 * @param post    第三方POST结果
	 * @param query   取值来自request.getParameterMap()方法
	 * @return
	 */
	String exchangeBack(int partner, String post, Map<String, String[]> query);
}
