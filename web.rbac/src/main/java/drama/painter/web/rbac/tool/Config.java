package drama.painter.web.rbac.tool;

import drama.painter.core.web.config.ElasticSearch;
import drama.painter.core.web.config.HttpLog;
import drama.painter.core.web.security.PageSecurityConfig;
import drama.painter.web.rbac.service.inf.IOa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebFilter;

/**
 * @author murphy
 */
@Component
public class Config {
	@Component
	public static class ElasticSearchClient extends ElasticSearch {
	}

	@Component
	public static class PageSecurity extends PageSecurityConfig {
		@Autowired
		public PageSecurity(IOa oa) {
			permissionProvider = () -> oa.getPage();
			userProvider = username -> oa.getStaff(username);
		}
	}

	@Component
	@WebFilter(urlPatterns = "*")
	static class PostFilter extends HttpLog {
	}
}

