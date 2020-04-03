package drama.painter.core.web.utility;

import lombok.SneakyThrows;
import org.apache.pulsar.shade.org.apache.commons.lang.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.Instant;

/**
 * @author murphy
 */
public class Strings {
	private static int INCREMENT = 0;
	private static final Object LOCK = new Object[0];

	public static String reset(String val) {
		return StringUtils.isEmpty(val) ? "" : val.trim();
	}


	public static long getOrderId(String prefix) {
		int value;
		synchronized (LOCK) {
			INCREMENT = INCREMENT == 999 ? 0 : ++INCREMENT;
			value = INCREMENT;
		}
		String time = Dates.toDigitalTime(Instant.now().getEpochSecond());
		String suffix = StringUtils.leftPad(String.valueOf(value), 3, '0');
		return Long.parseLong(prefix.concat(time).concat(suffix));
	}

	public static String toByteString(byte[] dest) {
		StringBuilder hexValue = new StringBuilder();
		for (byte md5Byte : dest) {
			int val = ((int) md5Byte) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	public static byte[] fromByteString(String src) {
		byte[] dest = new byte[src.length() / 2];
		for (int i = 0; i < dest.length; i++) {
			dest[i] = Byte.decode("0x" + src.substring(i * 2, i * 2 + 2));
		}
		return dest;
	}

	@SneakyThrows
	public static String urlencode(String text) {
		return URLEncoder.encode(text, "utf-8");
	}

	@SneakyThrows
	public static String urldecode(String text) {
		return URLDecoder.decode(text, "utf-8");
	}
}
