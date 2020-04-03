package drama.painter.web.mall.model.enums;

import drama.painter.core.web.enums.BaseEnum;

/**
 * @author murphy
 */
public enum CashTypeEnum implements BaseEnum {
	/**
	 * 最小金额
	 */
	MIN(1, "最小金额"),

	/**
	 * 最大金额
	 */
	MAX(2, "最大金额"),

	/**
	 * 固定金额
	 */
	FIXED(3, "固定金额");

	int value;
	String name;

	CashTypeEnum(int value, String name) {
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