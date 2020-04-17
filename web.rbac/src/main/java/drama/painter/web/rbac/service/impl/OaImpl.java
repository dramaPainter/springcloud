package drama.painter.web.rbac.service.impl;

import drama.painter.core.web.misc.Constant;
import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.web.rbac.service.inf.IOa;
import drama.painter.web.rbac.service.inf.sink.IOaSink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author murphy
 */
@Slf4j(topic = "api")
@Service
public class OaImpl implements IOa {
	final IOaSink oadb;

	static final List<User> STAFF = new ArrayList();
	static final List<Permission> PAGE = new ArrayList();

	@Autowired
	public OaImpl(IOaSink oadb) {
		this.oadb = oadb;
		STAFF.addAll(oadb.getStaff());
		PAGE.addAll(oadb.getPage());
	}

	@Override
	public Result<List<User>> getStaff(int page) {
		int from = (Math.max(page, 1) - 1) * Constant.PAGE_SIZE;
		int to = from + Constant.PAGE_SIZE > STAFF.size() ? STAFF.size() - 1 : from + Constant.PAGE_SIZE;
		return Result.toData(STAFF.size(), STAFF.subList(from, to));
	}

	@Override
	public User getStaff(String username) {
		return STAFF.stream()
			.filter(o -> o.getUsername().equals(username))
			.findAny()
			.orElse(null);
	}

	@Override
	public List<Permission> getPage() {
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
