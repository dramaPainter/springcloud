package drama.painter.web.mall.model.dto.order;

import lombok.Data;
import drama.painter.web.mall.model.enums.RechargeStatusEnum;
import drama.painter.web.mall.model.enums.RechargeMethodEnum;

/**
 * @author murphy
 */
@Data
public class RechargeDTO {
	/**
	 * 订单号
	 */
	String orderid;

	/**
	 * 帐号ID
	 */
	Integer userid;

	/**
	 * 第三方支付合作商ID
	 */
	Short partner;

	/**
	 * 下单时间
	 */
	String fixdate;

	/**
	 * 回调时间
	 */
	String echodate;

	/**
	 * 状态 1.代理充值 2.已支付 3.未支付 4.退款中 5.已退款 6.退款失败 7.下单出错 8.回调出错 9.系统内部错误
	 */
	RechargeStatusEnum status;

	/**
	 * 付款金额(单位:分)
	 */
	Integer cash;

	/**
	 * 支付方式 1.代理支付 2.支付宝支付 3.微信支付 4.云闪付 5.QQ支付 6、苹果支付
	 */
	RechargeMethodEnum method;

	/**
	 * IP地址
	 */
	Long ip;

	/**
	 * 第三方订单ID
	 */
	String thirdid;
}
