package drama.painter.web.rbac.controller;

import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.web.rbac.service.inf.IOa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author murphy
 */
@RestController
public class OaController {
	private final IOa oa;

	@Autowired
	public OaController(IOa oa) {
		this.oa = oa;
	}

	@GetMapping("/oa/staff")
	public Result<List<User>> staff(int page) {
		return oa.getStaff(page);
	}
}
