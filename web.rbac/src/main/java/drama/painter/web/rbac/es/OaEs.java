package drama.painter.web.rbac.es;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;

/**
 * @author murphy
 */
@Slf4j(topic = "api")
@Service
public class OaEs {
    public static BoolQueryBuilder listOperations(String startTime, String endTime, Integer timespan, String text) {
        BoolQueryBuilder builder = new BoolQueryBuilder();
        builder.must().add(new RangeQueryBuilder("timestamp").gte(startTime).lt(endTime));
        builder.must().add(new RangeQueryBuilder("timespan").gte(timespan));
        if (!StringUtils.isEmpty(text)) {
            Arrays.asList(text.split(" ")).stream().forEach(s ->
                    builder.must().add(new MultiMatchQueryBuilder(s, "project", "username", "url", "ip", "parameter"))
            );

        }
        return builder;
    }
}
