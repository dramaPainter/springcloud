package drama.painter.web.rbac.service.inf;

import drama.painter.core.web.misc.Permission;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;

import java.util.List;

/**
 * @author murphy
 */
public interface IOa {
    /**
     * 所有员工资料列表
     *
     * @param page 第几页
     * @return
     */
    Result<List<User>> getStaff(int page);

    /**
     * 根据员工帐号查询员工资料
     *
     * @param username 员工帐号
     * @return
     */
    User getStaff(String username);

    /**
     * 所有页面列表
     *
     * @return
     */
    List<Permission> getPage();

    /**
     * 重新加载缓存
     */
    void reset();
}
