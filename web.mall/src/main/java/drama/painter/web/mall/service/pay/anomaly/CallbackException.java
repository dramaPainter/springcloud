package drama.painter.web.mall.service.pay.anomaly;

/**
 * @author murphy
 * 请求返回的Code值不正确
 */
public class CallbackException extends RuntimeException {
	public CallbackException(String message) {
		super(message);
	}
}
