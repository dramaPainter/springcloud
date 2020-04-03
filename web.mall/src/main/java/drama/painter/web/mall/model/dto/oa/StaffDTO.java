package drama.painter.web.mall.model.dto.oa;

import lombok.Data;

/**
 * @author murphy
 */
@Data
public class StaffDTO {
	/**
	 * 帐号ID
	 */
	Integer userid;

	/**
	 * 帐号名称
	 */
	String username;

	/**
	 * 帐号密码
	 */
	String loginpass;

	/**
	 * 动态码
	 */
	String salt;

	/**
	 * 昵称
	 */
	String nickname;

	/**
	 * 头像
	 */
	String headimage;

	/**
	 * 状态：1.启用 2.冻结 3.删除
	 */
	Byte status;

	/**
	 * 帐号类型：127.全功能 100.组长 6.兑换订单客服帐号 5.兑换客服帐号 4.充值客服帐号 3.平台帐号 1.普通客服帐号
	 */
	Byte type;

	/**
	 * 平台ID 255､全平台 100､有限平台（限一三平台） 其他为单平台
	 */
	Integer platform;

	/**
	 * 登录IP限制
	 */
	String ip;

	/**
	 * 访问权限
	 */
	String permission;
}
