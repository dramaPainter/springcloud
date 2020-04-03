package drama.painter.core.web.tool;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author murphy
 */
public final class Json {
	static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
			.configure(SerializationFeature.INDENT_OUTPUT, true);
	}

	/**
	 * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
	 * (1)转换为普通JavaBean：fromJSON(json, Student.class)
	 * (2)转换为数组,如Student[],将第二个参数传递为Student[].class
	 */
	public static <T> T parseObject(String json, Class<T> valueType) {
		try {
			return mapper.readValue(json, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 使用泛型方法，把json字符串转换为相应的内部泛形成员对象。
	 * 如Result&lt;List&lt;Result>>,将第二个参数传递为
	 * new TypeReference&lt;Result&lt;List&lt;Result>>>(){}
	 */
	public static <T> T parseObject(String json, TypeReference<T> reference) {
		try {
			return mapper.readValue(json, reference);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 使用泛型方法，把json字符串转换为相应的JavaBean对象。
	 * (1)转换为普通JavaBean：fromJSON(json, Student.class)
	 * (2)转换为数组,如Student[],将第二个参数传递为Student[].class
	 */
	public static <T> T parseByteObject(byte[] json, Class<T> valueType) {
		try {
			return mapper.readValue(json, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 使用泛型方法，把json字符串转换为相应的内部泛形成员对象。
	 * 如Result&lt;List&lt;Result>>,将第二个参数传递为
	 * new TypeReference&lt;Result&lt;List&lt;Result>>>(){}
	 */
	public static <T> T parseByteObject(byte[] json, TypeReference<T> reference) {
		try {
			return mapper.readValue(json, reference);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String toJsonString(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}