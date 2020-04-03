package drama.painter.web.mall.service.pay.tool;

import drama.painter.core.web.misc.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author murphy
 */
@Component
public class Notify {
	@Autowired
	private NotifyFailOver notify;

	/**
	 * 玩家充值
	 */
	public Result sendPayInfo(long id, int userid, long recharge, int vip, int vip2, long cash, long totalCash) {
		int dwMainId = 50;
		int dwSubId = 5000;
		String json = "{\"userid\":" + userid + ",\"recharge\":" + recharge + ",\"vip\":" + vip + ",\"vip2\":" + vip2 + ",\"cash\":" + cash + ",\"totalCash\":" + totalCash + ",\"type\":1}";
		return notify.merge(id, dwMainId, dwSubId, json);
	}

	/**
	 * 通知新邮件
	 */
	public Result notifyMail(int userid) {
		int dwMainId = 50;
		int dwSubId = 5028;
		String config = String.format("{\"userid\":%d}", userid);
		return notify.merge(0, dwMainId, dwSubId, config);
	}
}
