package drama.painter.realtime.delivery.model;

import lombok.Data;

/**
 * @author murphy
 */
@Data
public class ChatPO {
	/**
	 * 自动增长列
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
