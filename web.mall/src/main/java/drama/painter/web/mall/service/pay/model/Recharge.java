package drama.painter.web.mall.service.pay.model;

import drama.painter.web.mall.service.pay.enums.MethodEnum;
import drama.painter.web.mall.service.pay.enums.RechargeStatusEnum;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author murphy
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Recharge extends Order {
	/**
	 * 1.代理充值 2.已支付 3.未支付 4.退款中 5.已退款 6.退款失败 7.下单出错 8.回调出错 9.系统内部错误
	 */
	private RechargeStatusEnum status;

	@Builder
	public Recharge(Long orderid, Integer userid, Integer partner, String fixdate, String echodate, Integer cash, MethodEnum method, String ip, String thirdid, String callback, RechargeStatusEnum status) {
		super(orderid, userid, partner, fixdate, echodate, cash, method, ip, thirdid, callback);
		this.status = status;
	}
}

