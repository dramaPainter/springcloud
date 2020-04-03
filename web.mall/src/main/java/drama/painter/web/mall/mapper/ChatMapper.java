package drama.painter.web.mall.mapper;

import drama.painter.core.web.misc.Page;
import drama.painter.web.mall.model.dto.oa.ChatDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author murphy
 */
@Repository
public interface ChatMapper {
	/**
	 * 查询聊天消息
	 *
	 * @param chat 聊天对象
	 * @param page 分页
	 * @return
	 */
	@Select({"<script>",
		"SELECT * FROM zero.chat WHERE fixdate >= #{chat.fixdate}",
		"<if test='chat.sender != null'>AND sender = #{chat.sender}</if>",
		"<if test='chat.receiver != null'>AND receiver = #{chat.receiver}</if>",
		"<if test='chat.view != null'>AND view = #{chat.view}</if>",
		"<if test='chat.kefu != null'>AND sender = #{chat.kefu}</if>",
		"<if test='chat.reply != null'>AND sender = #{chat.reply}</if>",
		"<if test='chat.text != null'>AND sender = #{chat.text}</if>",
		"</script>"})
	List<ChatDTO> getChatList(@Param("chat") ChatDTO chat, Page page);

	/**
	 * 查询聊天消息
	 *
	 * @param userid 玩家ID
	 * @return
	 */
	@Select("SELECT * FROM zero.chat WHERE fixdate > DATE_ADD(CURDATE(), INTERVAL -1 DAY) AND (sender = #{userid} OR receiver = #{userid}) ORDER BY fixdate DESC LIMIT 100")
	List<ChatDTO> getUserChatList(@Param("userid") int userid);

	/**
	 * 快捷回复列表
	 *
	 * @return
	 */
	@Select("SELECT concat(a.name, '|', b.name) FROM treasure.options a INNER JOIN treasure.options b on a.pid = b.id where a.pid > 0")
	List<String> getShortcut();

	/**
	 * 保存聊天消息
	 *
	 * @param chat 聊天对象
	 * @return
	 */
	@Options(useGeneratedKeys = true, keyProperty = "id")
	@Insert("INSERT INTO zero.chat VALUES(DEFAULT, NOW(), #{chat.sender}, #{chat.receiver}, #{chat.view}, #{chat.text}, #{chat.kefu}, #{chat.reply}, #{chat.body})")
	int save(@Param("chat") ChatDTO chat);

	/**
	 * 应答消息为已读
	 *
	 * @param sender   是否为客服
	 * @param receiver 帐号ID
	 * @param reply    是否已处理
	 * @return
	 */
	@Update({"<script>",
		"UPDATE zero.chat SET view = 1",
		"<if test='reply'>, reply = 1</if>",
		"WHERE fixdate > DATE_ADD(CURDATE(), INTERVAL -1 DAY)",
		"<if test='sender != null'>AND sender = #{sender}</if>",
		"<if test='receiver != null'>AND receiver = #{receiver}</if>",
		"</script>"})
	int acknowlege(@Param("sender") Integer sender, @Param("receiver") Integer receiver, @Param("reply") boolean reply);
}
