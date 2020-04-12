package drama.painter.web.rbac.service.impl.db;

import drama.painter.web.rbac.mapper.OAMapper;
import drama.painter.web.rbac.model.dto.oa.PageDTO;
import drama.painter.web.rbac.model.dto.oa.StaffDTO;
import drama.painter.web.rbac.service.inf.sink.IOASink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author murphy
 */
@Service("oadb")
public class OADBImpl implements IOASink {
	@Autowired
	OAMapper oaMapper;

	@Override
	public List<StaffDTO> getStaff() {
		return oaMapper.getStaff();
	}

	@Override
	public List<PageDTO> getPage() {
		return oaMapper.getPage();
	}
}
