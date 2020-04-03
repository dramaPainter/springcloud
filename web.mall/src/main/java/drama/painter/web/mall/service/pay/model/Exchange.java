package drama.painter.web.mall.service.pay.model;

import drama.painter.web.mall.service.pay.enums.ExchangeStatusEnum;
import drama.painter.web.mall.service.pay.enums.ExchangeUserTypeEnum;
import drama.painter.web.mall.service.pay.enums.MethodEnum;
import lombok.*;

/**
 * @author murphy
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Exchange extends Order {
	/**
	 * 状态：等待兑换(4), 人工审核(6), 审核中(8), 已驳回(9), 准备兑换(10), 兑换失败(15), 兑换成功(18), 已拒绝(20), 退款成功(24), 退款中(25), 退款失败(26)
	 */
	private ExchangeStatusEnum status;
	/**
	 * 应收金额（单位：分）
	 */
	private Integer price;
	/**
	 * 打款人类型 1、玩家 2、全民 3、地推
	 */
	private ExchangeUserTypeEnum type;
	/**
	 * 银行卡号 / 支付号帐号
	 */
	private String account;
	/**
	 * 银行地址：XX省XX市XX支行
	 */
	private String address;
	/**
	 * 真实姓名
	 */
	private String realname;

	@Builder
	public Exchange(Long orderid, Integer userid, Integer partner, String fixdate, String echodate, Integer cash, MethodEnum method, String ip, String thirdid, String callback, ExchangeStatusEnum status, Integer price, ExchangeUserTypeEnum type, String account, String address, String realname) {
		super(orderid, userid, partner, fixdate, echodate, cash, method, ip, thirdid, callback);
		this.status = status;
		this.price = price;
		this.type = type;
		this.account = account;
		this.address = address;
		this.realname = realname;
	}

	public void setOrderStatus(Long orderid, ExchangeStatusEnum status, String callback) {
		this.orderid = orderid;
		this.status = status;
		this.callback = callback;
	}
}

