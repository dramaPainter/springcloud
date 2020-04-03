package drama.painter.web.mall.service.pay.anomaly;

/**
 * @author murphy
 * 订单已经处理过了
 */
public class OrderProccessedException extends RuntimeException {
	public OrderProccessedException(String message) {
		super(message);
	}
}
