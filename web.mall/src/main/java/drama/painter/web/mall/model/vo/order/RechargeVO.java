package drama.painter.web.mall.model.vo.order;

import drama.painter.web.mall.model.enums.RechargeMethodEnum;
import drama.painter.web.mall.model.enums.RechargeStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author murphy
 */
@AllArgsConstructor
@Data
public class RechargeVO {
	String orderid;
	Integer userid;
	String partner;
	String fixdate;
	String echodate;
	RechargeStatusEnum status;
	Integer amount;
	RechargeMethodEnum method;
	Long ip;
	String thirdid;
}

