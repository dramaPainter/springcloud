package drama.painter.web.mall.service.pay.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author murphy
 */
@NoArgsConstructor
@Data
public class Mail {
	private int mailid;
	private int userid;
	private int channelid;
	private String title;
	private String content;
	private int gem;
	private long score;
	private byte scoreType;
	private byte mailType;
	private int adminid;
	private byte state;
	private String sendTime;
	private String expireTime;

	public Mail(int userid, int adminid, long score, String title, String content) {
		this.userid = userid;
		this.adminid = adminid;
		this.score = score;
		this.title = title;
		this.content = content;
	}
}