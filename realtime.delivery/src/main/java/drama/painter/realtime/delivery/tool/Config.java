package drama.painter.realtime.delivery.tool;

import drama.painter.core.web.config.*;
import drama.painter.core.web.tool.HttpLog;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebFilter;

/**
 * @author murphy
 */
@Component
public class Config extends DefaultJackson2JavaTypeMapper {
	@Component
	public class ElasticSearchClient extends ElasticSearch {
	}

	@Configuration
	public class KafkaConfig extends Kafka {
	}

	@Configuration
	public class WebSecurity extends SingleSecurity {
	}
}