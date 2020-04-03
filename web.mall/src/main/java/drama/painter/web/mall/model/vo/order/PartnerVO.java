package drama.painter.web.mall.model.vo.order;

import drama.painter.web.mall.model.enums.RechargeMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.EnumMap;
import java.util.List;

/**
 * @author murphy
 */
@NoArgsConstructor
@Data
public class PartnerVO {
	public PartnerVO(Short id, String name, Integer peak) {
		this.id = id;
		this.name = name;
		this.peak = peak;
	}

	Short id;
	String name;
	Integer peak;

	Boolean alipay;
	Boolean wxpay;
	Boolean unionpay;
	Boolean qqpay;
	Boolean applepay;

	Boolean display;
	Boolean recharge;
	Boolean foreign;
	Boolean vip;

	EnumMap<RechargeMethodEnum, PartnerRangeVO> range;

	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	public static class PartnerRangeVO {
		Short id;
		RechargeMethodEnum method;
		Boolean enabled;
		Integer min;
		Integer max;
		List<Integer> fixed;
	}
}

