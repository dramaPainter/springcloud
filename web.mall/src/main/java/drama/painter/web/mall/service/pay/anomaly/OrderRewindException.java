package drama.painter.web.mall.service.pay.anomaly;

/**
 * @author murphy
 * 订单驳回
 */
public class OrderRewindException extends RuntimeException {
	public OrderRewindException(String message) {
		super(message);
	}
}
