package drama.painter.web.mall.model.enums;

import drama.painter.core.web.enums.BaseEnum;

/**
 * @author murphy
 */
public enum RechargeStatusEnum implements BaseEnum {
	/**
	 * 所有状态
	 */
	ALL(0, "所有状态"),

	/**
	 * 代理充值
	 */
	AGENT(1, "代理充值"),

	/**
	 * 已付款
	 */
	PAID(2, "已付款"),

	/**
	 * 未付款
	 */
	UNPAID(3, "未付款"),

	/**
	 * 退款中
	 */
	REFUNDING(4, "退款中"),

	/**
	 * 已退款
	 */
	REFUNDED(5, "已退款"),

	/**
	 * 退款失败
	 */
	UNREFUNDABLE(6, "退款失败"),

	/**
	 * 下单出错
	 */
	CREATE_ERROR(7, "下单出错"),

	/**
	 * 回调出错
	 */
	CALLBACK_ERROR(8, "回调出错"),

	/**
	 * 系统内部错误
	 */
	SYSTEM_ERROR(9,"系统内部错误");

	int value;
	String name;

	RechargeStatusEnum(int value, String name) {
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