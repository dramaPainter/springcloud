package drama.painter.web.rbac.service.impl.db;

import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.User;
import drama.painter.web.rbac.mapper.OaMapper;
import drama.painter.web.rbac.service.inf.sink.IOaSink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author murphy
 */
@Service("oadb")
public class OaDbImpl implements IOaSink {
	final OaMapper oaMapper;

	@Autowired
	public OaDbImpl(OaMapper oaMapper) {
		this.oaMapper = oaMapper;
	}

	@Override
	public List<User> getStaff() {
		return oaMapper.getStaff();
	}

	@Override
	public List<Permission> getPage() {
		return oaMapper.getPage();
	}
}
