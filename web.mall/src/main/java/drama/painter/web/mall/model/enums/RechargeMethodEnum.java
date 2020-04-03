package drama.painter.web.mall.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import drama.painter.core.web.enums.BaseEnum;
import drama.painter.core.web.enums.EnumConverter;

/**
 * @author murphy
 */
public enum RechargeMethodEnum implements BaseEnum {
	/**
	 * 所有通道
	 */
	ALL(0, "所有通道"),

	/**
	 * 微信支付
	 */
	AGENTPAY(1, "代理支付"),

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

	int value;
	String name;

	@JsonCreator
	public static RechargeMethodEnum create(String code) {
		return EnumConverter.toEnum(RechargeMethodEnum.class, code);
	}

	RechargeMethodEnum(int value, String name) {
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