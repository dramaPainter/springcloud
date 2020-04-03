package drama.painter.web.mall.service.pay.anomaly;

/**
 * @author murphy
 * 兑换渠道已经全部关闭
 */
public class ServiceUnavailableException extends RuntimeException {
	public ServiceUnavailableException(String message) {
		super(message);
	}
}
