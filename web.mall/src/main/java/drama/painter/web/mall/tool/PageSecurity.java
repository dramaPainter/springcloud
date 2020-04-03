package drama.painter.web.mall.tool;

import drama.painter.core.web.misc.User;
import drama.painter.web.mall.model.dto.oa.StaffDTO;
import drama.painter.web.mall.mapper.StaffMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import drama.painter.core.web.config.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author murphy
 */

@Configuration
@EnableWebSecurity
class PageSecurity extends WebSecurity {
	@Autowired
	StaffMapper staffMapper;

	public PageSecurity(){
		this.userProvider = username -> {
			StaffDTO staff = staffMapper.getStaff(username);
			if (staff == null) {
				return null;
			} else {
				User user = new User();
				BeanUtils.copyProperties(staff, user);
				user.setPassword(staff.getLoginpass());
				return user;
			}
		};
	}
}