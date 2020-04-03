package drama.painter.web.mall.service.pay.anomaly;

/**
 * @author murphy
 * 订单不存在
 */
public class OrderNotFoundException extends RuntimeException {
	public OrderNotFoundException(String message) {
		super(message);
	}
}
