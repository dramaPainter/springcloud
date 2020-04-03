package drama.painter.web.mall.service.pay.model;

import lombok.Data;

/**
 * @author murphy
 */
@Data
public class Bank {
	private String name;
	private String province;
	private String city;
	private String branch;
	private String card;
	private String bankCode;

	public Bank(String name, String province, String city, String branch, String card, String bankCode) {
		this.name = name;
		this.province = province;
		this.city = city;
		this.branch = branch;
		this.card = card;
		this.bankCode = bankCode;
	}
}