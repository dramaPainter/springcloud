package drama.painter.web.rbac.service.impl;

import drama.painter.web.rbac.model.dto.oa.PageDTO;
import drama.painter.web.rbac.model.dto.oa.StaffDTO;
import drama.painter.web.rbac.service.inf.IOA;
import drama.painter.web.rbac.service.inf.sink.IOASink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author murphy
 */
@Slf4j(topic = "api")
@Service
public class OAImpl implements IOA {
	@Autowired
	IOASink oadb;

	static final List<StaffDTO> STAFF = new ArrayList();
	static final List<PageDTO> PAGE = new ArrayList();

	@PostConstruct
	public void init() {
		STAFF.addAll(oadb.getStaff());
		PAGE.addAll(oadb.getPage());
	}

	@Override
	public StaffDTO getStaff(String username) {
		return STAFF.stream().filter(o -> o.getUsername().equals(username)).findAny().orElse(null);
	}

	@Override
	public List<PageDTO> getPage() {
		return PAGE;
	}

	@Override
	public void reset() {
		synchronized (STAFF) {
			STAFF.clear();
			STAFF.addAll(oadb.getStaff());
		}

		synchronized (PAGE) {
			PAGE.clear();
			PAGE.addAll(oadb.getPage());
		}
	}
}
