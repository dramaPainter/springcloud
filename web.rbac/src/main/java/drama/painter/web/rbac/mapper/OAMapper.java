package drama.painter.web.rbac.mapper;

import drama.painter.web.rbac.model.dto.oa.PageDTO;
import drama.painter.web.rbac.model.dto.oa.StaffDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author murphy
 */
@Mapper
public interface OAMapper {
	/**
	 * 查询所有员工帐号
	 *
	 * @return
	 */
	@Select({"SELECT *, ",
		"(SELECT GROUP_CONCAT(INET_NTOA(ip)) FROM oa.staff_ip si WHERE si.userid = s.userid) AS ip, ",
		"(SELECT GROUP_CONCAT(pageid) FROM oa.staff_page sp WHERE sp.userid = s.userid) AS permission ",
		"FROM oa.staff s WHERE status = 1"})
	List<StaffDTO> getStaff();

	/**
	 * 查询所有页面
	 *
	 * @return
	 */
	@Select({"SELECT * FROM oa.page WHERE status = 1"})
	List<PageDTO> getPage();
}
