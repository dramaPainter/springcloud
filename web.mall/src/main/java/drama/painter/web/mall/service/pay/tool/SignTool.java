package drama.painter.web.mall.service.pay.tool;

import drama.painter.core.web.tool.Json;
import drama.painter.core.web.utility.Encrypts;
import drama.painter.core.web.utility.Strings;
import drama.painter.web.mall.service.pay.anomaly.UnsupportedException;
import drama.painter.web.mall.service.pay.enums.EncryptEnum;
import drama.painter.web.mall.service.pay.enums.SerializeEnum;
import drama.painter.web.mall.service.pay.model.Bank;
import drama.painter.web.mall.service.pay.model.Exchange;
import drama.painter.web.mall.service.pay.model.Order;

import java.util.HashMap;
import java.util.Map;

/**
 * @author murphy
 * 充值服务
 */
public class SignTool {
	private static final Map<SerializeEnum, ISerializeResolver> SERIALIZER = new HashMap(2);
	private static final Map<SerializeEnum, ISubstituteResolver> SUBSTITUTER = new HashMap(2);
	private static final Map<EncryptEnum, IEncryptResolver> ENCRYPTER = new HashMap(2);

	static {
		SERIALIZER.put(SerializeEnum.JSON, new JsonSerializer());
		SERIALIZER.put(SerializeEnum.URL, new UrlSerializer());
		SUBSTITUTER.put(SerializeEnum.JSON, new JsonSubstitute());
		SUBSTITUTER.put(SerializeEnum.URL, new UrlSubstitute());
		ENCRYPTER.put(EncryptEnum.MD5_TEXT, new Md5Resolver());
		ENCRYPTER.put(EncryptEnum.RSA_TEXT, new RsaResolver());
	}

	public static ISubstituteResolver getSubstitute(SerializeEnum resolver) {
		return SUBSTITUTER.get(resolver);
	}

	public static ISerializeResolver getSerializer(SerializeEnum resolver) {
		return SERIALIZER.get(resolver);
	}

	public static IEncryptResolver getEncrypter(EncryptEnum method) {
		return ENCRYPTER.get(method);
	}

	private static String replaceAddress(String text, Order order, String appid, String callbackUrl) {
		text = text.replaceFirst("<orderid>", String.valueOf(order.getOrderid()))
			.replaceFirst("<callbackUrl>", callbackUrl)
			.replaceFirst("<ip>", order.getIp())
			.replaceFirst("<appid>", appid);

		boolean isExchange = order instanceof Exchange;
		if (isExchange) {
			Bank bank;
			Exchange exchange = (Exchange)order;

			text = text.replaceFirst("<account>", exchange.getAccount())
				.replaceFirst("<realname>", exchange.getRealname());

			boolean hasProvince = text.contains("<province>");
			if (hasProvince) {
				try {
					bank = BankTool.getRegion(exchange.getAccount(), exchange.getAddress());
				} catch (UnsupportedException e) {
					throw new RuntimeException(e);
				}

				text = text.replaceFirst("<province>", bank.getProvince())
					.replaceFirst("<city>", bank.getCity())
					.replaceFirst("<branch>", bank.getBranch())
					.replaceFirst("<bankcode>", bank.getBankCode())
					.replaceFirst("<account>", exchange.getAccount())
					.replaceFirst("<realname>", exchange.getRealname());
			}
		}

		return text;
	}

	private static class JsonSubstitute implements ISubstituteResolver {
		@Override
		public String apply(String text, Order order, String appid, String callbackUrl) {
			text = text
				.replaceFirst("\"<cash%df>\"", String.valueOf(order.getCash()))
				.replaceFirst("\"<cash%dy>\"", String.valueOf(order.getCash() / 100))
				.replaceFirst("<cash%sf>", String.valueOf(order.getCash()))
				.replaceFirst("<cash%sy>", String.valueOf(order.getCash() / 100))
				.replaceFirst("\"<userid%d>\"", String.valueOf(order.getUserid()))
				.replaceFirst("<userid%s>", String.valueOf(order.getUserid()));

			return replaceAddress(text, order, appid, callbackUrl);
		}
	}

	private static class UrlSubstitute implements ISubstituteResolver {
		@Override
		public String apply(String text, Order order, String appid, String callbackUrl) {
			text = text
				.replaceFirst("<cash%df>", String.valueOf(order.getCash()))
				.replaceFirst("<cash%dy>", String.valueOf(order.getCash() / 100))
				.replaceFirst("<cash%sf>", String.valueOf(order.getCash()))
				.replaceFirst("<cash%sy>", String.valueOf(order.getCash() / 100))
				.replaceFirst("<userid%d>", String.valueOf(order.getUserid()))
				.replaceFirst("<userid%s>", String.valueOf(order.getUserid()));
			return replaceAddress(text, order, appid, callbackUrl);
		}
	}

	private static class JsonSerializer implements ISerializeResolver {
		@Override
		public String apply(Map<String, Object> map, boolean encoding) {
			return Json.toJsonString(map);
		}
	}

	private static class UrlSerializer implements ISerializeResolver {
		@Override
		public String apply(Map<String, Object> map, boolean encoding) {
			return map.entrySet().stream()
				.map(p -> p.getKey().concat("=").concat(encoding ? Strings.urlencode(p.getValue().toString()) : p.getValue().toString()))
				.reduce((p1, p2) -> p1.concat("&").concat(p2))
				.orElse("");
		}
	}

	private static class Md5Resolver implements IEncryptResolver {
		@Override
		public String apply(String text) {
			return Encrypts.md5(text).toUpperCase();
		}
	}

	private static class RsaResolver implements IEncryptResolver {
		@Override
		public String apply(String text) {
			throw new RuntimeException("暂不支持此功能。");
		}
	}

	@FunctionalInterface
	public interface ISerializeResolver {
		/**
		 * 待签名文本序列化的方法
		 *
		 * @param map      序列化的MAP对象
		 * @param encoding 是否编码
		 * @return
		 */
		String apply(Map<String, Object> map, boolean encoding);
	}

	@FunctionalInterface
	public interface ISubstituteResolver {
		/**
		 * 待签名文本替换内容
		 *
		 * @param text        签名文本
		 * @param order       订单对象
		 * @param callbackUrl 回调网址
		 * @param appid       appid
		 * @return
		 */
		String apply(String text, Order order, String appid, String callbackUrl);
	}

	@FunctionalInterface
	public interface IEncryptResolver {
		/**
		 * 执行加密操作
		 *
		 * @param text 签名文本
		 * @return
		 */
		String apply(String text);
	}
}
