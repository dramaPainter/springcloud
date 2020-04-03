package drama.painter.web.mall.controller;

import drama.painter.core.web.tool.HttpLog;
import drama.painter.web.mall.service.pay.impl.PayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author murphy
 */
@Slf4j
@RestController
public class PayController {
	@Autowired
	PayServiceImpl service;

	@PostMapping("/pay/callback/{partner}")
	public String callback(@PathVariable("partner") int partner) {
		return service.exchangeBack(partner, HttpLog.getPost(), HttpLog.getParameterMap());
	}
}
