package drama.painter.web.mall.service.inf;

import drama.painter.core.web.misc.Result;
import drama.painter.core.web.pulsar.zero.ChatPO;

import java.util.List;

/**
 * @author murphy
 */
public interface IChat {
	/**
	 * 保存聊天记录
	 *
	 * @param sender   发送人
	 * @param receiver 接收人
	 * @param body     内容
	 * @param kefu     是否为客服
	 * @param name     客服帐号
	 */
	void saveMessage(Integer sender, Integer receiver, String body, Boolean kefu, String name);

	/**
	 * 应答消息
	 *
	 * @param sender   发送人
	 * @param receiver 接收人
	 * @param reply    是否已处理
	 */
	void acknowlege(Integer sender, Integer receiver, boolean reply);

	/**
	 * 批量回复消息
	 *
	 * @param sender   发送人
	 * @param receiver 接收人
	 * @param body     内容
	 */
	void batchReply(Integer sender, List<Integer> receiver, String body);

	/**
	 * 读取聊天记录
	 *
	 * @param chatpo 搜索条件
	 * @return
	 */
	List<ChatPO> readMessage(ChatPO chatpo);

	/**
	 * 打字提示
	 *
	 * @param key 搜索内容
	 * @return
	 */
	List<Result> searchAssistant(String key);

	/**
	 * 读取玩家聊天记录
	 *
	 * @param userid 玩家ID
	 * @return
	 */
	List<ChatPO> readUserMessage(int userid);
}
