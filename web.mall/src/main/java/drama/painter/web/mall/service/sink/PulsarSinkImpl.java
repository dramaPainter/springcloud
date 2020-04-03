package drama.painter.web.mall.service.sink;

import drama.painter.core.web.misc.Page;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.pulsar.zero.ChatPO;
import drama.painter.web.mall.service.inf.ISink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import drama.painter.web.mall.tool.Config;

import java.util.List;

/**
 * @author murphy
 */
@Slf4j(topic = "chat")
@Service("pulsarSink")
public class PulsarSinkImpl implements ISink {
	@Autowired
	Config.PulsarClient pulsar;

	@Autowired
	ISink esSink;

	@Override
	public List<ChatPO> getChatList(ChatPO chatpo, Page page) {
		return esSink.getChatList(chatpo, page);
	}

	@Override
	public List<ChatPO> getUserChatList(int userid) {
		return esSink.getUserChatList(userid);
	}

	@Override
	public void save(ChatPO chatpo) {
		pulsar.send("persistent://web/zero/chat", "save", chatpo, ChatPO.class);
	}

	@Override
	public void acknowlege(Integer sender, Integer receiver, boolean reply) {
		ChatPO chatpo = new ChatPO();
		chatpo.setSender(sender);
		chatpo.setReceiver(receiver);
		chatpo.setReply(reply);
		pulsar.send("persistent://web/zero/chat", "acknowlege", chatpo, ChatPO.class);
	}

	@Override
	public List<Result> searchAssistant(String key) {
		return null;
	}
}
