package drama.painter.server.config.tool;

import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.stream.IntStream;

/**
 * @author murphy
 */
public class Decrypt {
	static final int HALF = 2;
	static BigInteger m_key;

	static {
		m_key = new BigInteger("0");
		String config = "201805010099999";
		char[] key = (config + "\0").toCharArray();
		int len = key.length;
		for (int i = 0; i < len; i++) {
			BigInteger big = new BigInteger("2").pow(8 * (i % 8)).multiply(new BigInteger(String.valueOf((byte) key[i])));
			m_key = m_key.add(big);
		}
	}

	public static String decrypt(byte[] source) {
		byte mask = m_key.and(new BigInteger("255")).byteValue();
		int len = source.length;
		byte[] dest = new byte[len];
		dest[0] = (byte) (source[0] ^ mask);

		for (int i = 1; i < len; i++) {
			dest[i] = (byte) (source[i] ^ mask);
			mask = dest[mask % i];
		}

		return new String(dest);
	}

	public static String decrypt(String text) {
		try {
			if (StringUtils.isEmpty(text) || ((text.length() % HALF) == 1)) {
				return "";
			}
			byte[] source = new byte[text.length() / 2];
			IntStream.range(0, text.length() / HALF).forEach(i -> source[i] = (byte) (Integer.parseInt(text.substring(i * 2, i * 2 + 2), 16) & 0xff));
			return decrypt(source);
		} catch (Exception e) {
			return null;
		}
	}
}
