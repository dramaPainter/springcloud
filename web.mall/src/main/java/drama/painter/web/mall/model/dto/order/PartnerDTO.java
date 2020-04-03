package drama.painter.web.mall.model.dto.order;

import lombok.Data;

/**
 * @author murphy
 */
@Data
public class PartnerDTO {
	/**
	 * 合作商ID
	 */
	Short id;

	/**
	 * 合作商名称
	 */
	String name;

	/**
	 * 1.代理支付 2.支付宝支付 3.微信支付 4.云闪付 5.QQ支付 6.苹果支付
	 */
	Byte method;

	/**
	 * 1.显示/隐藏 2.支付/兑换 4.外部/内部 8.VIP/非VIP
	 */
	Byte property;

	/**
	 * 监控当天最高充值金额，超过自动禁掉，0为不限制
	 */
	Integer peak;
}

