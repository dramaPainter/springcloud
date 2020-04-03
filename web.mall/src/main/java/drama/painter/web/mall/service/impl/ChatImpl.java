package drama.painter.web.mall.service.impl;

import drama.painter.core.web.misc.Constant;
import drama.painter.core.web.misc.Page;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.core.web.pulsar.zero.ChatPO;
import drama.painter.core.web.utility.Dates;
import drama.painter.core.web.validator.ImageValidator;
import drama.painter.core.web.validator.UrlValidator;
import drama.painter.core.web.validator.Validator;
import drama.painter.web.mall.service.inf.IChat;
import drama.painter.web.mall.service.inf.ISink;
import drama.painter.web.mall.tool.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author murphy
 */
@Slf4j(topic = "chat")
@Service
public class ChatImpl implements IChat {
	static final String ACKNOWLEGE = "{\"receiver\":%d}";
	static final Validator URL_VALIDATOR = new UrlValidator();
	static final Validator IMAGE_VALIDATOR = new ImageValidator();
	static final Map<Integer, User> USERS = new ConcurrentHashMap();

	@Autowired
	ISink esSink;

	@Autowired
	SimpMessagingTemplate sms;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveMessage(Integer sender, Integer receiver, String body, Boolean kefu, String name) {
		boolean text = !URL_VALIDATOR.validate(body) || !IMAGE_VALIDATOR.validate(body);
		ChatPO chatpo = new ChatPO(0, Dates.getNowDateTime(), sender, receiver, body, false, text, kefu, false);
		name = setReceiver(chatpo, name);
		esSink.save(chatpo);
		esSink.acknowlege(chatpo.getReceiver(), null, true);
		sms.convertAndSend("/kefu/" + name, chatpo);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void batchReply(Integer sender, List<Integer> receiver, String body) {
		ChatPO chatpo = new ChatPO(null, Dates.getNowDateTime(), sender, null, body, false, true, true, true);
		for (Integer rec : receiver) {
			chatpo.setReceiver(rec);
			esSink.save(chatpo);
			esSink.acknowlege(rec, null, true);
		}
	}

	@Override
	public void acknowlege(Integer sender, Integer receiver, boolean reply) {
		esSink.acknowlege(sender, receiver, reply);
		User user = USERS.get(receiver);
		if (user != null) {
			sms.convertAndSend("/kefu/" + user.getUsername(), String.format(ACKNOWLEGE, receiver));
		}
	}

	@Override
	public List<Result> searchAssistant(String key) {
		return esSink.searchAssistant(key);
	}

	@Override
	public List<ChatPO> readMessage(ChatPO chatpo) {
		return esSink.getChatList(chatpo, new Page(1, Constant.PAGE_SIZE));
	}

	@Override
	public List<ChatPO> readUserMessage(int userid) {
		return esSink.getUserChatList(userid);
	}

	public static void removeUser(int userid) {
		Iterator<Map.Entry<Integer, User>> it = USERS.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, User> entry = it.next();
			if (entry.getValue().getUserid().equals(userid)) {
				it.remove();
			}
		}
	}

	private String setReceiver(ChatPO chatpo, String name) {
		if (chatpo.getKefu()) {
			return name;
		} else {
			User property = USERS.get(chatpo.getSender());
			if (property == null) {
				property = WebSocket.getUser();
				if (property.getUserid() > 0) {
					USERS.put(chatpo.getSender(), property);
				}
			}
			chatpo.setReceiver(property.getUserid());
			return property.getUsername();
		}
	}
}
