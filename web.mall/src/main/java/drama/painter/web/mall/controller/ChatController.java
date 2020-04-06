package drama.painter.web.mall.controller;

import drama.painter.core.web.config.WebSecurity;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.web.mall.model.po.ChatPO;
import drama.painter.web.mall.service.inf.IChat;
import drama.painter.web.mall.service.inf.IUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author murphy
 */
@Controller
public class ChatController {
	@Autowired
	IChat dbChat;

	@Autowired
	IUpload upload;

	@GetMapping("/chat/index")
	public String index(Map<String, Object> map, UsernamePasswordAuthenticationToken principal) {
		map.put("username", principal.getName());
		return "chat/index";
	}

	/**
	 * 查询玩家消息
	 */
	@ResponseBody
	@PostMapping("/chat/searchAssistant")
	public List<Result> searchAssistant(String key) {
		return dbChat.searchAssistant(key);
	}

	/**
	 * 查询玩家消息
	 */
	@ResponseBody
	@GetMapping("/chat/message")
	public List<ChatPO> message(int userid) {
		return dbChat.readUserMessage(userid);
	}

	/**
	 * 客服发送消息
	 */
	@ResponseBody
	@PostMapping("/chat/send")
	public Result send(int receiver, String body) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = ((WebSecurity.PageUserDetails) principal).getUser();
		dbChat.saveMessage(user.getUserid(), receiver, body, true, user.getUsername());
		return Result.SUCCESS;
	}

	/**
	 * 客服接收消息
	 */
	@ResponseBody
	@PostMapping("/chat/receive")
	public Result receive(int sender, String body) {
		dbChat.saveMessage(sender, null, body, false, null);
		return Result.SUCCESS;
	}

	/**
	 * 玩家发送确认消息
	 */
	@ResponseBody
	@PostMapping("/chat/acknowlege")
	public Result acknowlege(int userid) {
		dbChat.acknowlege(null, userid, false);
		return Result.SUCCESS;
	}

	@MessageExceptionHandler(Exception.class)
	@SendToUser("/chat/error")
	public Exception handleExceptions(Exception e) {
		System.out.println("############## 错误 ############# - " + e.getMessage());
		return e;
	}
}
