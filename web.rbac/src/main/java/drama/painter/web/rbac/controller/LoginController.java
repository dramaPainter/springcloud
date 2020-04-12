package drama.painter.web.rbac.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author murphy
 */
@Controller
public class LoginController {
	@GetMapping("/login/login")
	public String login() {
		return "login/login";
	}

	@GetMapping("/login/error")
	public String error() {
		return "login/error";
	}
}
