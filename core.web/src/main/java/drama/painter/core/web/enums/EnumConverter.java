package drama.painter.core.web.enums;

import drama.painter.core.web.validator.IntegerValidator;
import drama.painter.core.web.validator.Validator;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * @author murphy
 */
@Component
public class EnumConverter implements ConverterFactory<String, BaseEnum> {
	static final Validator INTEGER = new IntegerValidator();

	/**
	 * 根据名称或者值返回该枚举对象
	 *
	 * @param targerType 枚举的class类型
	 * @param value      枚举值，可以是名称，也可以是值
	 * @param <T>        枚举类型
	 * @return
	 */
	public static <T extends BaseEnum> T toEnum(Class<T> targerType, String value) {
		Predicate<T> predicate = INTEGER.validate(value) ? (x -> x.getValue() == Integer.parseInt(value)) : (x -> ((Enum)x).name().equals(value));
		return Arrays.stream(targerType.getEnumConstants()).filter(predicate).findAny().orElse(null);
	}

	@Override
	public <T extends BaseEnum> Converter<String, T> getConverter(@NonNull Class<T> targetType) {
		return new StringToEum(targetType);
	}

	static class StringToEum<T extends BaseEnum> implements Converter<String, T> {
		final Class<T> targerType;

		public StringToEum(Class<T> targerType) {
			this.targerType = targerType;
		}

		@Override
		public T convert(@NonNull String source) {
			if (StringUtils.isEmpty(source)) {
				return null;
			}
			return EnumConverter.toEnum(targerType, source);
		}
	}
}
