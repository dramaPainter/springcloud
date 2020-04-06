package drama.painter.web.mall.service.pay.interfaces;

/**
 * @author murphy
 */
public interface INotify {
	/**
	 * 发送邮件通知
	 * @param userid 玩家ID
	 */
	void notifyMail(int userid);
}
