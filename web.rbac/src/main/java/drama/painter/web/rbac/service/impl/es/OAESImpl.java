package drama.painter.web.rbac.service.impl.es;

import drama.painter.web.rbac.model.dto.oa.PageDTO;
import drama.painter.web.rbac.model.dto.oa.StaffDTO;
import drama.painter.web.rbac.service.inf.sink.IOASink;
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
public class OAESImpl implements IOASink {
	static AtomicInteger ID;

	@Autowired
	Config.ElasticSearchClient es;

	@PostConstruct
	public void init() {
		ID = es.max("zero-chat", "id");
	}

	@Override
	public List<StaffDTO> getStaff() {
		return es.list(new SearchRequest("oa-staff"), StaffDTO.class);
	}

	@Override
	public List<PageDTO> getPage() {
		return es.list(new SearchRequest("oa-page"), PageDTO.class);
	}
}
