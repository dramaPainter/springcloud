package drama.painter.web.mall.service.pay.anomaly;

/**
 * @author murphy
 * 兑换渠道不存在
 */
public class ServiceNotFoundException extends RuntimeException {
	public ServiceNotFoundException(String message) {
		super(message);
	}
}
