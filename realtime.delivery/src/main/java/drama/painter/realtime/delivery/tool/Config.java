package drama.painter.realtime.delivery.tool;

import drama.painter.core.web.config.ElasticSearch;
import drama.painter.core.web.security.BasicSecurityConfig;
import org.springframework.kafka.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.stereotype.Component;

/**
 * @author murphy
 */
@Component
public class Config extends DefaultJackson2JavaTypeMapper {
    @Component
    public static class ElasticSearchClient extends ElasticSearch {
    }

    @Component
    public static class PageSecurity extends BasicSecurityConfig {
    }
}