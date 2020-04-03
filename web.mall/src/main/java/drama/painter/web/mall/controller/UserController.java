package drama.painter.web.mall.controller;

import drama.painter.web.mall.service.inf.IChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author murphy
 */
@Controller
public class UserController {
	@Autowired
	IChat staff;

	@GetMapping("/login/login")
	public String login() {
		return "login/login";
	}
}
