package drama.painter.core.web.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import drama.painter.core.web.config.ElasticSearch;
import drama.painter.core.web.utility.Dates;

import java.util.HashMap;
import java.util.Map;

/**
 * @author murphy
 */
class SqlLogAppender implements IAppender {
    @Override
    public void append(ElasticSearch client, ILoggingEvent event) {
        Object[] args = event.getArgumentArray();
        String timestamp = Dates.toDateTimeMillis();
        String index = "sql-" + timestamp.substring(0, 7).replaceFirst("-", "");

        Map map = new HashMap();
        map.put("timestamp", timestamp);
        map.put("project", args[0]);
        map.put("countTime", args[1]);
        map.put("queryTime", args[2]);
        map.put("sql", args[3]);

        try {
            client.create(index, null, map);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            map.clear();
        }
    }
}