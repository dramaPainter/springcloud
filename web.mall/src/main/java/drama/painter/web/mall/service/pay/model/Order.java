package drama.painter.web.mall.service.pay.model;

import drama.painter.web.mall.service.pay.enums.MethodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author murphy
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
	/**
	 * 订单号
	 */
	protected Long orderid;

	/**
	 * 帐号ID
	 */
	private Integer userid;

	/**
	 * 第三方支付合作商ID
	 */
	private Integer partner;

	/**
	 * 下单时间
	 */
	private String fixdate;

	/**
	 * 回调时间
	 */
	private String echodate;

	/**
	 * 实收金额（单位：分）
	 */
	private Integer cash;

	/**
	 * 1.代理支付 2.支付宝支付 4.微信支付 8.云闪付 16.QQ支付 32.苹果支付
	 */
	private MethodEnum method;

	/**
	 * IP地址
	 */
	private String ip;

	/**
	 * 第三方订单ID
	 */
	private String thirdid;

	/**
	 * 回调信息
	 */
	protected String callback;
}

