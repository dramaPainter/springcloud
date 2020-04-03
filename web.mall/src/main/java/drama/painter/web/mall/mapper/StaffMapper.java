package drama.painter.web.mall.mapper;

import drama.painter.web.mall.model.dto.oa.StaffDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author murphy
 */
@Repository
public interface StaffMapper {
	/**
	 * 合作商列表
	 *
	 * @param username 客服帐号
	 * @return
	 */
	@Select({"SELECT *, " ,
		"(SELECT GROUP_CONCAT(INET_NTOA(ip)) FROM oa.staff_ip si WHERE si.userid = s.userid) AS ip, ",
		"(SELECT GROUP_CONCAT(pageid) FROM oa.staff_page sp WHERE sp.userid = s.userid) AS permission ",
		"FROM oa.staff s WHERE username = #{username} LIMIT 1"})
	StaffDTO getStaff(@Param("username") String username);
}
