package drama.painter.web.mall.service.pay.enums;

import drama.painter.core.web.enums.BaseEnum;

/**
 * @author murphy
 */
public enum RechargeStatusEnum implements BaseEnum {
	/**
	 * 代理充值
	 */
	AGENT(1, "代理充值"),
	/**
	 * 已支付
	 */
	PAID(2, "已支付"),
	/**
	 * 未支付
	 */
	UNPAID(3, "未支付"),
	/**
	 * 退款中
	 */
	REFUND(4, "退款中"),
	/**
	 * 已退款
	 */
	REFUNDED(5, "已退款"),
	/**
	 * 退款失败
	 */
	UNREFUND(6, "退款失败"),
	/**
	 * 下单出错
	 */
	UNDERGO(7, "下单出错"),
	/**
	 * 回调出错
	 */
	UNCALLBACKABLE(8, "回调出错"),
	/**
	 * 系统内部错误
	 */
	ERROR(9, "系统内部错误");

	private int id;
	private String name;

	RechargeStatusEnum(int id, String name) {
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

