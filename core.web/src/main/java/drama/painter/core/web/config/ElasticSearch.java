package drama.painter.core.web.config;

import drama.painter.core.web.tool.Certification;
import drama.painter.core.web.tool.Json;
import drama.painter.core.web.utility.Encrypts;
import drama.painter.core.web.utility.Strings;
import lombok.Data;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author murphy
 */
public class ElasticSearch {
	@Autowired
	RestHighLevelClient client;

	public void create(String index, String id, Object object) {
		IndexRequest source = new IndexRequest(index).id(id).source(Json.toJsonString(object), XContentType.JSON);
		try {
			IndexResponse response = client.index(source, RequestOptions.DEFAULT);
			if (response.status() != RestStatus.CREATED) {
				throw new IOException(response.status().name());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> List<T> list(SearchRequest request, Class<T> type) {
		try {
			SearchResponse response = client.search(request, RequestOptions.DEFAULT);
			if (response.status() == RestStatus.OK) {
				return Arrays.asList(response.getHits().getHits()).stream()
					.map(x -> Json.parseObject(x.getSourceAsString(), type))
					.collect(Collectors.toList());
			} else {
				throw new IOException(response.status().name());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public <T> T read(SearchRequest request, Class<T> type) {
		try {
			SearchResponse response = client.search(request, RequestOptions.DEFAULT);
			if (response.status() == RestStatus.OK) {
				SearchHit[] hits = response.getHits().getHits();
				if (hits != null && hits.length > 0) {
					return Json.parseObject(hits[0].getSourceAsString(), type);
				} else {
					return null;
				}
			} else {
				throw new IOException(response.status().name());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void delete(String index, String id) {
		DeleteRequest source = new DeleteRequest(index).id(id);
		try {
			DeleteResponse response = client.delete(source, RequestOptions.DEFAULT);
			if (response.status() == RestStatus.ACCEPTED) {
				throw new IOException(response.status().name());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void update(UpdateByQueryRequest request) {
		try {
			BulkByScrollResponse response = client.updateByQuery(request, RequestOptions.DEFAULT);
			if (response.getTotal() != response.getStatus().getSuccessfullyProcessed()) {
				long replyed = response.getStatus().getSuccessfullyProcessed();
				String msg = String.format("有部分消息没有处理:%d(总:%d)", replyed, response.getTotal());
				throw new IOException(msg);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void bulk(BulkRequest bulk) {
		try {
			BulkResponse response = client.bulk(bulk, RequestOptions.DEFAULT);
			if (response.status() == RestStatus.ACCEPTED) {
				throw new IOException(response.status().name());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			bulk.requests().clear();
		}
	}

	public long count(SearchRequest request) {
		CountRequest count = new CountRequest().query(request.source().query());
		try {
			CountResponse response = client.count(count, RequestOptions.DEFAULT);
			if (response.status() == RestStatus.OK) {
				return response.getCount();
			} else {
				throw new IOException(response.status().name());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public SearchResponse search(SearchRequest request) {
		try {
			SearchResponse response = client.search(request, RequestOptions.DEFAULT);
			if (response.status() != RestStatus.OK) {
				throw new IOException(response.status().name());
			}
			return response;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public SearchResponse search(String index, BoolQueryBuilder query, TermsAggregationBuilder field) {
		SearchSourceBuilder builder = new SearchSourceBuilder().size(0);
		if (field != null) {
			builder.aggregation(field);
		}
		SearchRequest source = new SearchRequest(index).source(builder.query(query));
		try {
			return search(source);
		} finally {
			if (query != null) {
				query.must().clear();
				query.should().clear();
				query.filter().clear();
				query.mustNot().clear();
			}
		}
	}
}

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
class ElasticEntity {
	String user;
	String password;
	List<String> hosts;
	String cert;
}


@Component
class ElasticConfig {
	@Autowired
	ElasticEntity entity;

	static final int TIMEOUT = 5;

	@Primary
	@Bean
	RestHighLevelClient restHighLevelClient() {
		BasicCredentialsProvider provider = new BasicCredentialsProvider();
		provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(entity.getUser(), Encrypts.decrypt(entity.getPassword())));

		HttpHost[] hosts = new HttpHost[entity.getHosts().size()];
		for (int i = 0; i < hosts.length; i++) {
			hosts[i] = HttpHost.create(entity.getHosts().get(i));
		}

		return new RestHighLevelClient(RestClient.builder(hosts)
			.setRequestConfigCallback(x ->
				x.setSocketTimeout(TIMEOUT * 1000).setConnectTimeout(TIMEOUT * 1000).setConnectionRequestTimeout(TIMEOUT * 1000 * 4)
			).setHttpClientConfigCallback(x ->
				x.setDefaultCredentialsProvider(provider)
				.setSSLContext(Certification.parse(Encrypts.decrypt(entity.getCert()))
			)));
	}
}