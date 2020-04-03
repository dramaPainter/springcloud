package drama.painter.web.mall.service.pay.enums;

import drama.painter.core.web.enums.BaseEnum;

/**
 * @author murphy
 */
public enum ExchangeUserTypeEnum implements BaseEnum {
	/**
	 * 玩家
	 */
	USER(1, "玩家"),
	/**
	 * 全民
	 */
	QUANMIN(4, "全民"),

	/**
	 * 地推
	 */
	DITUI(3,"地推");

	private int id;
	private String name;

	ExchangeUserTypeEnum(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public int getValue() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}
}

