package drama.painter.web.mall.model.dto.oa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author murphy
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatDTO {
	/**
	 * 消息ID
	 */
	Integer id;

	/**
	 * 时间
	 */
	String fixdate;

	/**
	 * 发送人
	 */
	Integer sender;

	/**
	 * 接收人
	 */
	Integer receiver;

	/**
	 * 内容
	 */
	String body;

	/**
	 * 已读(未读)
	 */
	Boolean view;

	/**
	 * 文字(图片)
	 */
	Boolean text;

	/**
	 * 客服消息(玩家消息)
	 */
	Boolean kefu;

	/**
	 * 已处理(未处理)
	 */
	Boolean reply;
}