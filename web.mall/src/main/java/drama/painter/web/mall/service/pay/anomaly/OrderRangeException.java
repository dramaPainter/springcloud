package drama.painter.web.mall.service.pay.anomaly;

/**
 * @author murphy
 * 订单金额超出范围
 */
public class OrderRangeException extends RuntimeException {
	public OrderRangeException(String message) {
		super(message);
	}
}
