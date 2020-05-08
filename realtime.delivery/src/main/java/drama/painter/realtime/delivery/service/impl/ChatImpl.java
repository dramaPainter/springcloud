package drama.painter.realtime.delivery.service.impl;

import drama.painter.realtime.delivery.model.ChatPO;
import drama.painter.realtime.delivery.service.inf.IChat;
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
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author murphy
 */
@Slf4j(topic = "pulsar")
@Service
public class ChatImpl implements IChat {
    static final AtomicInteger ID = new AtomicInteger(0);
    final Config.ElasticSearchClient es;

    public ChatImpl(Config.ElasticSearchClient es) {
        this.es = es;
    }

    @PostConstruct
    public void init() {
        SearchSourceBuilder builder = new SearchSourceBuilder().size(1).sort("fixdate", SortOrder.DESC);
        SearchRequest request = new SearchRequest("zero-chat").source(builder);
        ChatPO result = es.read(request, ChatPO.class);
        if (result != null) {
            ID.set(result.getId());
        }
    }

    @Override
    public void save(ChatPO chatpo, String index) {
        if (Objects.isNull(chatpo.getId())) {
            // 回复单个玩家消息
            chatpo.setId(ID.incrementAndGet());
            es.create(index, String.valueOf(chatpo.getId()), chatpo);
        } else {
            // 回复多个玩家消息
            BulkRequest bulkRequest = new BulkRequest(index);
            ChatPO instance = new ChatPO();
            BeanUtils.copyProperties(chatpo, instance);

            BoolQueryBuilder boolBuilder = new BoolQueryBuilder();
            UpdateByQueryRequest updateRequest = new UpdateByQueryRequest(index)
                    .setScript(new Script("ctx._source['reply'] = true"))
                    .setQuery(boolBuilder)
                    .setRefresh(true);

            String id = String.valueOf(ID.incrementAndGet());
            instance.setId(Integer.parseInt(id));
            instance.setReceiver(chatpo.getReceiver());
            bulkRequest.add(new IndexRequest(index).id(id).source(instance));
            boolBuilder.must().clear();
            boolBuilder.must().add(new TermQueryBuilder("sender", chatpo.getSender()));
            es.update(updateRequest);
        }
    }
}
