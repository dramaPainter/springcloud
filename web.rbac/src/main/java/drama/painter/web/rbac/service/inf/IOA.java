package drama.painter.web.rbac.service.inf;

import drama.painter.web.rbac.model.dto.oa.PageDTO;
import drama.painter.web.rbac.model.dto.oa.StaffDTO;

import java.util.List;

/**
 * @author murphy
 */
public interface IOA {
	/**
	 * 所有支付通道列表
	 *
	 * @return
	 */
	StaffDTO getStaff(String username);

	/**
	 * 查询充值列表
	 *
	 * @return
	 */
	List<PageDTO> getPage();

	/**
	 * 重新加载缓存
	 */
	void reset();
}
