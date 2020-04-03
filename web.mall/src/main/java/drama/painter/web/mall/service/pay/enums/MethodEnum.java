package drama.painter.web.mall.service.pay.enums;

import drama.painter.core.web.enums.BaseEnum;

/**
 * @author murphy
 */
public enum MethodEnum implements BaseEnum {
	/**
	 * 代理支付
	 */
	AGENT(1, "代理支付"),
	/**
	 * 支付宝支付
	 */
	ALIPAY(2, "支付宝支付"),
	/**
	 * 微信支付
	 */
	WXPAY(4, "微信支付"),
	/**
	 * 云闪付
	 */
	UNIONPAY(8, "云闪付"),
	/**
	 * QQ支付
	 */
	QQPAY(16, "QQ支付"),
	/**
	 * 苹果支付
	 */
	APPLEPAY(32, "苹果支付");

	private int id;
	private String name;

	MethodEnum(int id, String name) {
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

