package drama.painter.web.mall.service.pay.anomaly;

/**
 * @author murphy
 * 订单金额与实际金额不一致
 */
public class OrderInconsistentException extends RuntimeException {
	public OrderInconsistentException(String message) {
		super(message);
	}
}
