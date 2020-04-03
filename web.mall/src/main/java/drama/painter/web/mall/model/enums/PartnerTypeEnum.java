package drama.painter.web.mall.model.enums;

import drama.painter.core.web.enums.BaseEnum;

/**
 * @author murphy
 */
public enum PartnerTypeEnum implements BaseEnum {
	/**
	 * 显示(隐藏)
	 */
	DISPLAY(1, "显示"),

	/**
	 * 充值(兑换)
	 */
	RECHARGE(2, "充值"),

	/**
	 * 外部(内部)
	 */
	FOREIGN(4, "外部"),

	/**
	 * VIP通道(非VIP通道)
	 */
	VIP(8, "VIP通道");

	int value;
	String name;

	PartnerTypeEnum(int value, String name) {
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