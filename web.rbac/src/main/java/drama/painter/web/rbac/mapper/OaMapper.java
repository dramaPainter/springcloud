package drama.painter.web.rbac.mapper;

import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author murphy
 */
@Repository
public interface OaMapper {
	/**
	 * 查询所有员工帐号
	 *
	 * @return
	 */
	@Select({"SELECT userid, username, nickname, loginpass AS password, salt, headimage, status, ",
		"(SELECT GROUP_CONCAT(INET_NTOA(ip)) FROM oa.staff_ip si WHERE si.userid = s.userid) AS ip, ",
		"(SELECT GROUP_CONCAT(pageid) FROM oa.staff_page sp WHERE sp.userid = s.userid) AS permission ",
		"FROM oa.staff s"})
	List<User> getStaff();

	/**
	 * 查询所有页面
	 *
	 * @return
	 */
	@Select({"SELECT * FROM oa.page WHERE status = 1"})
	List<Permission> getPage();
}
