package drama.painter.core.web.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import drama.painter.core.web.config.ElasticSearch;

/**
 * @author murphy
 */
public interface IAppender {
    void append(ElasticSearch client, ILoggingEvent event);
}