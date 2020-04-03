package drama.painter.web.mall.service.pay.anomaly;

/**
 * @author murphy
 * 不支持的银行卡
 */
public class UnsupportedException extends RuntimeException {
	public UnsupportedException(String message) {
		super(message);
	}
}
