package drama.painter.web.mall.service.sink;

import drama.painter.core.web.misc.Page;
import drama.painter.core.web.misc.Result;
import drama.painter.web.mall.model.po.ChatPO;
import drama.painter.web.mall.service.inf.ISink;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ParsedMax;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import drama.painter.web.mall.tool.Config;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author murphy
 */
@Slf4j(topic = "chat")
@Service("esSink")
public class ElasticSearchSinkImpl implements ISink {
	static AtomicInteger ID = null;
	static final String INDEX = "zero-chat";
	@Autowired
	Config.ElasticSearchClient es;

	@PostConstruct
	public void init() {
		MaxAggregationBuilder max = AggregationBuilders.max("最大值").field("id");
		SearchResponse search = es.search(new SearchRequest(INDEX).source(new SearchSourceBuilder().aggregation(max)));
		double value = ((ParsedMax) search.getAggregations().get("最大值")).getValue();
		value = Double.isInfinite(value) ? 0 : value;
		ID = new AtomicInteger((int)value);
	}

	@Override
	public List<ChatPO> getChatList(ChatPO chatpo, Page page) {
		BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
		if (chatpo.getSender() != null) {
			queryBuilder.should().add(new TermQueryBuilder("sender", chatpo.getSender()));
		}

		queryBuilder.should().add(new TermQueryBuilder("reply", chatpo.getReply()));
		queryBuilder.should().add(new RangeQueryBuilder("fixdate").from(chatpo.getFixdate()));
		SearchSourceBuilder builder = new SearchSourceBuilder().query(queryBuilder).size(page.getSize()).sort("id", SortOrder.DESC);
		return getList(builder, queryBuilder);
	}

	@Override
	public List<ChatPO> getUserChatList(int userid) {
		BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
		queryBuilder.should().add(new TermQueryBuilder("sender", userid));
		queryBuilder.should().add(new TermQueryBuilder("receiver", userid));
		SearchSourceBuilder builder = new SearchSourceBuilder().query(queryBuilder).size(100).sort("id", SortOrder.DESC);
		return getList(builder, queryBuilder);
	}

	@Override
	public void save(ChatPO chatpo) {
		chatpo.setId(ID.incrementAndGet());
		es.create(INDEX, String.valueOf(chatpo.getId()), chatpo);
	}

	@Override
	public List<Result> searchAssistant(String key) {
		MatchQueryBuilder queryBuilder = new MatchQueryBuilder("body", key);
		SearchSourceBuilder builder = new SearchSourceBuilder().query(queryBuilder).size(10);
		SearchRequest request = new SearchRequest(INDEX).source(builder);
		List<ChatPO> list = es.list(request, ChatPO.class);
		List<Result> result = list.stream().map(o -> new Result(o.getId(), o.getBody())).collect(Collectors.toList());
		list.clear();
		return result;
	}

	@Override
	public void acknowlege(Integer sender, Integer receiver, boolean reply) {
		BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
		queryBuilder.must().add(new TermQueryBuilder("view", false));
		if (sender != null) {
			queryBuilder.must().add(new TermQueryBuilder("sender", sender));
		} else if (receiver != null) {
			queryBuilder.must().add(new TermQueryBuilder("receiver", receiver));
		}
		String script = "ctx._source.view = true".concat(reply ? "; ctx._source.reply = true" : "");
		UpdateByQueryRequest request = new UpdateByQueryRequest(INDEX)
			.setRefresh(true)
			.setQuery(queryBuilder)
			.setScript(new Script(script));
		es.update(request);
		queryBuilder.should().clear();
		queryBuilder.must().clear();
	}

	private List<ChatPO> getList(SearchSourceBuilder builder, BoolQueryBuilder query) {
		SearchRequest request = new SearchRequest(INDEX).source(builder);
		List<ChatPO> result = es.list(request, ChatPO.class);
		Collections.sort(result, Comparator.comparingInt(ChatPO::getId));
		query.should().clear();
		return result;
	}
}
