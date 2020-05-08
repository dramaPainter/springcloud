package drama.painter.core.web.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import drama.painter.core.web.config.ElasticSearch;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author murphy
 */
@Data
public class ElasticSearchLogAppender extends AppenderBase<ILoggingEvent> {
    static final Map<LogFormat, IAppender> MAP = new HashMap();
    protected static ElasticSearch client;

    static {
        MAP.put(LogFormat.API, new ApiLogAppender());
        MAP.put(LogFormat.HTTP, new HttpLogAppender());
        MAP.put(LogFormat.SQL, new SqlLogAppender());
    }

    LogFormat format;

    @Override
    protected void append(ILoggingEvent event) {
        IAppender appender = MAP.get(format);
        appender = appender == null ? MAP.get(LogFormat.API) : appender;
        appender.append(client, event);
    }
}
