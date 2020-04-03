package drama.painter.web.mall.model.vo.search;

import drama.painter.web.mall.model.enums.RechargeStatusEnum;
import lombok.Data;

/**
 * @author murphy
 */
@Data
public class RechargeSearch {
	String from;
	String to;
	RechargeStatusEnum status;
	int partner;
	int userid;
	String orderid;
	String thirdid;
	String text;
	int page;
}

