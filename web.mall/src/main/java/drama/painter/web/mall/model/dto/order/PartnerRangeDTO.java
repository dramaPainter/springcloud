package drama.painter.web.mall.model.dto.order;

import drama.painter.web.mall.model.enums.CashTypeEnum;
import drama.painter.web.mall.model.enums.RechargeMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author murphy
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PartnerRangeDTO {
	public PartnerRangeDTO(Integer amount) {
		this.amount = amount;
	}

	Short id;
	RechargeMethodEnum method;
	CashTypeEnum type;
	Integer amount;
}

