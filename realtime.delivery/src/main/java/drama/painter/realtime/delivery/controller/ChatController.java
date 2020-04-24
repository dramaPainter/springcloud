package drama.painter.realtime.delivery.controller;

import drama.painter.realtime.delivery.model.ChatPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author murphy
 */
@Slf4j
@RestController
public class ChatController {
	final KafkaTemplate<String, ChatPO> kafka;

	public ChatController(KafkaTemplate<String, ChatPO> kafka) {
		this.kafka = kafka;
	}

	@RequestMapping("/chat")
	public String chat(String text) {
		ChatPO chat = new ChatPO();
		chat.setBody(text);
		kafka.send("chat", chat).addCallback( t-> t.getProducerRecord().value(), e -> log.error("发送聊天信息出错", e));
		return text;
	}
}
