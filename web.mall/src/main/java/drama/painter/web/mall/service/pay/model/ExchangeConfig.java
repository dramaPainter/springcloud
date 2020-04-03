package drama.painter.web.mall.service.pay.model;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author murphy
 */
@RefreshScope
@Component
@Data
@ConfigurationProperties(prefix = "exchange")
public class ExchangeConfig {
	@Getter
	private Map<Integer, App> serviceProvider = new HashMap<>();


	@Data
	public static class App {
		/**
		 * APPID
		 */
		private String appId;
		/**
		 * APPKEY
		 */
		private String appKey;
		/**
		 * 附加值
		 */
		private String appValue;
		/**
		 * API接口网址
		 */
		private String url;
		/**
		 * 签名模版
		 */
		private String sign;
		/**
		 * 公钥
		 */
		private String publicKey;
		/**
		 * 私钥
		 */
		private String privateKey;
	}
}

