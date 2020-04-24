package drama.painter.core.web.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import drama.painter.core.web.config.ElasticSearch;
import drama.painter.core.web.utility.Dates;

import java.util.HashMap;
import java.util.Map;

/**
 * @author murphy
 */
class HttpLogAppender implements IAppender {
    @Override
    public void append(ElasticSearch client, ILoggingEvent event) {
        Object[] args = event.getArgumentArray();
        String timestamp = Dates.toDateTimeMillis();
        String index = "operation-" + timestamp.substring(0, 7).replaceFirst("-", "");

        Map map = new HashMap();
        map.put("timestamp", timestamp);
        map.put("project", args[0]);
        map.put("timespan", args[1]);
        map.put("username", args[2]);
        map.put("session", args[3]);
        map.put("ip", args[4]);
        map.put("url", args[5]);
        map.put("parameter", args[6]);
        map.put("result", args[7]);

        try {
            client.create(index, null, map);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            map.clear();
        }
    }
}