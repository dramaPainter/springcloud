package realtime.delivery.service.impl;

import drama.painter.core.web.config.Pulsar;
import drama.painter.core.web.pulsar.zero.ChatPO;
import drama.painter.core.web.validator.UrlValidator;
import drama.painter.core.web.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
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
import realtime.delivery.tool.Config;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author murphy
 */
@Slf4j(topic = "pulsar")
public class ChatProccessorImpl implements Pulsar.IDataProccessor {
	static final Validator URL_VALIDATOR = new UrlValidator();
	static final AtomicInteger ID = new AtomicInteger(0);
	Config.ElasticSearchClient es;

	public ChatProccessorImpl(Config.ElasticSearchClient es) {
		SearchSourceBuilder builder = new SearchSourceBuilder().size(1).sort("fixdate", SortOrder.DESC);
		SearchRequest request = new SearchRequest("zero-chat").source(builder);
		ChatPO result = es.read(request, ChatPO.class);
		if (result != null) {
			ID.set(result.getId());
		}
	}

	@Override
	public <T> void reply(Consumer<T> consumer, Message<T> msg) {
		String id = msg.getMessageId().toString();
		String index = consumer.getTopic().replace("persistent://web/", "").replace("/", "-");
		ChatPO chatpo = (ChatPO) msg.getValue();
		log.info("[接收]消息[{}][{}] - {}", id, index, chatpo);

		if (chatpo.getKefu()) {
			saveKefuMessage(chatpo, index);
		} else {
			//saveUserMessage(chatpo, index);
		}

		consumer.acknowledgeAsync(msg).thenAcceptAsync(v -> log.info("[接收]消息[{}]已确认。", id));
	}

	private void saveKefuMessage(ChatPO chatpo, String index) {
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
