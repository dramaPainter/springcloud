package drama.painter.web.rbac.service.inf.sink;

import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.User;

import java.util.List;

/**
 * @author murphy
 */
public interface IOaSink {
	/**
	 * 所有支付通道列表
	 *
	 * @return
	 */
	List<User> getStaff();

	/**
	 * 查询充值列表
	 *
	 * @return
	 */
	List<Permission> getPage();
}
