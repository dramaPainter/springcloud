package drama.painter.core.web.enums;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.FormatterRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author murphy
 */
@Component
class EnumRegister implements WebMvcConfigurer {
	@Autowired
	EnumConverter enumConvertFactory;

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverterFactory(enumConvertFactory);
	}
}

