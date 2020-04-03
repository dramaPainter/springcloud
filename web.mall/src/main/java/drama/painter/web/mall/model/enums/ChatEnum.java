package drama.painter.web.mall.model.enums;

import drama.painter.core.web.enums.BaseEnum;

/**
 * @author murphy
 */
public enum ChatEnum implements BaseEnum {
	/**
	 * 所有通道
	 */
	ALL(0, "所有通道"),
	/**
	 * 已读(未读)
	 */
	READ(1, "已读"),
	/**
	 * 文字(图片)
	 */
	TEXT(2, "文字"),
	/**
	 * 客服消息(玩家消息)
	 */
	KEFU(4, "客服消息"),
	/**
	 * 已处理(未处理)
	 */
	PROCCEED(8, "已处理");

	int value;
	String name;

	ChatEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public String getName() {
		return name;
	}
}