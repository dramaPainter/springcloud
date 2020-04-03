package drama.painter.web.mall.service.sink;

import drama.painter.core.web.misc.Page;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.pulsar.zero.ChatPO;
import drama.painter.web.mall.model.dto.oa.ChatDTO;
import drama.painter.web.mall.service.inf.ISink;
import drama.painter.web.mall.mapper.ChatMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author murphy
 */
@Slf4j(topic = "chat")
@Service("dbSink")
public class DatabaseSinkImpl implements ISink {
	private final int MAX_TIP_SIZE = 10;
	private static List<String> SHORTCUT = new ArrayList();
	@Autowired
	ChatMapper chatMapper;

	@PostConstruct
	public void init() {
		SHORTCUT.addAll(chatMapper.getShortcut());
	}

	@Override
	public List<ChatPO> getChatList(ChatPO chatpo, Page page) {
		ChatDTO chatDTO = convert(chatpo);
		List<ChatDTO> userChatList = chatMapper.getChatList(chatDTO, page);
		List<ChatPO> result = convert(userChatList);
		userChatList.clear();
		return result;
	}

	@Override
	public List<ChatPO> getUserChatList(int userid) {
		List<ChatDTO> userChatList = chatMapper.getUserChatList(userid);
		List<ChatPO> result = convert(userChatList);
		userChatList.clear();
		return result;
	}

	@Override
	public void save(ChatPO chatpo) {
		ChatDTO chatDTO = convert(chatpo);
		chatMapper.save(chatDTO);
	}

	@Override
	public List<Result> searchAssistant(String key) {
		List<Result> map = new ArrayList(MAX_TIP_SIZE);
		Arrays.asList(key.split("")).forEach(o ->
			SHORTCUT.stream().filter(t -> t.contains(o)).forEach(m -> {
				Optional<Result> any = map.stream().filter(n -> n.getMessage().equals(m)).findAny();
				if (any.isPresent()) {
					any.get().setCode(any.get().getCode() + 1);
				} else {
					map.add(new Result(1, m.substring(0, m.indexOf("|"))));
				}
			})
		);
		Collections.sort(map, (a, b) -> b.getCode() - a.getCode());
		if (map.size() > MAX_TIP_SIZE) {
			for (int i = map.size() - 1; i >= MAX_TIP_SIZE; i--) {
				map.remove(i);
			}
		}
		return map;
	}

	@Override
	public void acknowlege(Integer sender, Integer receiver, boolean reply) {
		chatMapper.acknowlege(sender, receiver, reply);
	}

	private List<ChatPO> convert(List<ChatDTO> userChatList) {
		List<ChatPO> result = userChatList.stream().map(o -> {
				ChatPO chatpo = new ChatPO();
				BeanUtils.copyProperties(o, chatpo);
				return chatpo;
			}
		).collect(Collectors.toList());
		userChatList.clear();
		Collections.sort(result, Comparator.comparingInt(ChatPO::getId));
		return result;
	}

	private ChatDTO convert(ChatPO chatpo) {
		ChatDTO chatDTO = new ChatDTO();
		BeanUtils.copyProperties(chatpo, chatDTO);
		return chatDTO;
	}
}
