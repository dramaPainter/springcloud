package drama.painter.web.mall.service.pay.anomaly;

/**
 * @author murphy
 * 订单拒绝
 */
public class OrderRefuseException extends RuntimeException {
	public OrderRefuseException(String message) {
		super(message);
	}
}
