package drama.painter.web.mall.service.pay.enums;

import drama.painter.core.web.enums.BaseEnum;

/**
 * @author murphy
 */
public enum ExchangeStatusEnum implements BaseEnum {
	/**
	 * 所有选项
	 */
	ALL(0, "所有选项"),

	/**
	 * 退款中
	 */
	REFUNDING(5, "退款中"),

	/**
	 * 人工审核（自动审核不通过将会转到人工审核）
	 */
	MANUAL(6, "人工审核"),

	/**
	 * 待审核(此时机器人会到点触发自动审核操作)
	 */
	UNAUTH(8, "未审核"),

	/**
	 * 等待兑换(审核通过后等待机器人到点触发自动兑换操作)
	 */
	WAIT(9, "等待兑换"),

	/**
	 * 准备兑换(兑换请求了第三方网址等待回调)
	 */
	READY(10, "准备兑换"),

	/**
	 * 已驳回
	 */
	REWIND(12, "已驳回"),

	/**
	 * 兑换失败
	 */
	FAIL(15, "兑换失败"),

	/**
	 * 兑换成功
	 */
	SUCCESS(18, "兑换成功"),

	/**
	 * 已拒绝
	 */
	REFUSE(20, "已拒绝"),

	/**
	 * 退款成功
	 */
	REFUND(24, "退款成功");

	private int value;
	private String name;

	ExchangeStatusEnum(int value, String name) {
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