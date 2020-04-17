package drama.painter.web.rbac.service.impl.es;

import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.User;
import drama.painter.web.rbac.service.inf.sink.IOaSink;
import drama.painter.web.rbac.tool.Config;
import org.elasticsearch.action.search.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author murphy
 */
@Service("oaes")
public class OaEsImpl implements IOaSink {
	static AtomicInteger ID;
	final Config.ElasticSearchClient es;

	@Autowired
	public OaEsImpl(Config.ElasticSearchClient es) {
		this.es = es;
	}

	@PostConstruct
	public void init() {
		ID = es.max("zero-chat", "id");
	}

	@Override
	public List<User> getStaff() {
		return es.list(new SearchRequest("oa-staff"), User.class);
	}

	@Override
	public List<Permission> getPage() {
		return es.list(new SearchRequest("oa-page"), Permission.class);
	}
}