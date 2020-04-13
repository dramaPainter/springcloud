package drama.painter.web.rbac.service.inf;

import drama.painter.core.web.misc.Result;
import drama.painter.web.rbac.model.dto.oa.PageDTO;
import drama.painter.web.rbac.model.dto.oa.StaffDTO;

import java.util.List;

/**
 * @author murphy
 */
public interface IOA {
	/**
	 * 所有员工资料列表
	 *
	 * @param page 第几页
	 * @return
	 */
	Result<List<StaffDTO>> getStaff(int page);

	/**
	 * 根据员工帐号查询员工资料
	 *
	 * @return
	 */
	StaffDTO getStaff(String username);

	/**
	 * 所有页面列表
	 *
	 * @return
	 */
	List<PageDTO> getPage();

	/**
	 * 重新加载缓存
	 */
	void reset();
}
