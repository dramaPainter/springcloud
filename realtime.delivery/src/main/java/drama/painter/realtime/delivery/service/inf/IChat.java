package drama.painter.realtime.delivery.service.inf;

import drama.painter.realtime.delivery.model.ChatPO;
import drama.painter.realtime.delivery.tool.Config;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author murphy
 */
public interface IChat {
	void save(ChatPO chatpo, String index);
}
