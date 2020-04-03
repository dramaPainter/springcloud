package drama.painter.web.mall.controller;

import drama.painter.web.mall.model.enums.RechargeStatusEnum;
import drama.painter.web.mall.model.vo.order.PartnerVO;
import drama.painter.web.mall.model.vo.search.RechargeSearch;
import drama.painter.web.mall.service.inf.IRecharge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import drama.painter.core.web.misc.Constant;
import drama.painter.core.web.misc.Page;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.utility.Dates;
import drama.painter.core.web.utility.Strings;
import drama.painter.core.web.validator.IntegerValidator;
import drama.painter.core.web.validator.Validator;

import java.util.Map;
import java.util.Objects;

/**
 * @author murphy
 */
@Controller
public class RechargeController {
	static final Validator INT_VALIDATOR = new IntegerValidator();
	static final String ORDER_PREFIX = "LRG";

	@Autowired
	IRecharge recharge;

	@GetMapping("/recharge/partner")
	public String partner(Map<String, Object> map) {
		map.put("src", recharge.getPartners());
		return "recharge/partner";
	}

	@GetMapping("/recharge/recharge")
	public String recharge(RechargeSearch search, Map<String, Object> map) {
		search.setFrom(Dates.toDateTime(Dates.getStartTime(search.getFrom())));
		search.setTo(Dates.toDateTime(Dates.getEndTime(search.getTo())));
		search.setText(Strings.reset(search.getText()));
		search.setStatus(Objects.isNull(search.getStatus()) ? RechargeStatusEnum.ALL : search.getStatus());

		if (StringUtils.isEmpty(search.getText())) {
		} else if (INT_VALIDATOR.validate(search.getText())) {
			search.setUserid(Integer.parseInt(search.getText()));
		} else if (search.getText().startsWith(ORDER_PREFIX)) {
			search.setOrderid(search.getText());
		} else {
			search.setThirdid(search.getText());
		}

		Page page = new Page(search.getPage() < 1 ? 1 : search.getPage(), Constant.PAGE_SIZE);
		map.put("src", recharge.getRecharges(search, page));
		map.put("partner", recharge.getPartners());
		map.put("status", RechargeStatusEnum.values());
		map.put("page", Constant.toPage(page.getPage(), page.getRowCount(), page.getSize(), true));
		return "recharge/recharge";
	}

	@ResponseBody
	@PostMapping("/recharge/update")
	public Result update(@RequestBody PartnerVO p) {
		recharge.updatePartner(p);
		return Result.SUCCESS;
	}

	@ResponseBody
	@PostMapping("/recharge/enable")
	public Result enable(@RequestBody PartnerVO.PartnerRangeVO p) {
		recharge.enableRecharge(p);
		return Result.SUCCESS;
	}
}
