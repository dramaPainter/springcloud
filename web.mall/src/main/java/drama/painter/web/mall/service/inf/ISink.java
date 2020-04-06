package drama.painter.web.mall.service.inf;

import drama.painter.core.web.misc.Page;
import drama.painter.core.web.misc.Result;
import drama.painter.web.mall.model.po.ChatPO;

import java.util.List;

/**
 * @author murphy
 */
public interface ISink {
	/**
	 * 查询聊天消息
	 *
	 * @param chat 聊天对象
	 * @param page 分页
	 * @return
	 */
	List<ChatPO> getChatList(ChatPO chat, Page page);

	/**
	 * 查询聊天消息
	 *
	 * @param userid 玩家ID
	 * @return
	 */
	List<ChatPO> getUserChatList(int userid);

	/**
	 * 快捷回复列表搜索
	 *
	 * @param key 搜索内容
	 * @return
	 */
	List<Result> searchAssistant(String key);

	/**
	 * 保存聊天消息
	 *
	 * @param chat 聊天对象
	 */
	void save(ChatPO chat);

	/**
	 * 应答消息
	 *
	 * @param sender   发送人
	 * @param receiver 接收人
	 * @param reply    是否已处理
	 */
	void acknowlege(Integer sender, Integer receiver, boolean reply);
}
