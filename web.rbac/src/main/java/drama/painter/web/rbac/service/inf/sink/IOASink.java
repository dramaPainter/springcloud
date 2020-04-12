package drama.painter.web.rbac.service.inf.sink;

import drama.painter.web.rbac.model.dto.oa.PageDTO;
import drama.painter.web.rbac.model.dto.oa.StaffDTO;

import java.util.List;
import java.util.Map;

/**
 * @author murphy
 */
public interface IOASink {
	/**
	 * 所有支付通道列表
	 *
	 * @return
	 */
	List<StaffDTO> getStaff();

	/**
	 * 查询充值列表
	 *
	 * @return
	 */
	List<PageDTO> getPage();
}
