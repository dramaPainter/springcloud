package drama.painter.web.rbac.model.dto.oa;

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
	 * 登录IP限制
	 */
	String ip;

	/**
	 * 访问权限
	 */
	String permission;
}
