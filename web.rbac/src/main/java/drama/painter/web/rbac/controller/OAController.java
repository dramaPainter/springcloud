package drama.painter.web.rbac.controller;

import drama.painter.core.web.misc.Result;
import drama.painter.web.rbac.model.dto.oa.StaffDTO;
import drama.painter.web.rbac.service.inf.IOA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author murphy
 */
@RestController
public class OAController {
	@Autowired

	IOA oa;

	@GetMapping("/oa/staff")
	public Result<List<StaffDTO>> staff(@RequestParam(name = "page", defaultValue = "1") int page) {
		return oa.getStaff(page);
	}
}
