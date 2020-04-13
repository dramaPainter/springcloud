package drama.painter.web.rbac.tool;

import drama.painter.core.web.config.WebSecurity;
import drama.painter.core.web.misc.Result;
import drama.painter.core.web.misc.User;
import drama.painter.core.web.tool.Json;
import drama.painter.web.rbac.model.dto.oa.PageDTO;
import drama.painter.web.rbac.model.dto.oa.StaffDTO;
import drama.painter.web.rbac.service.inf.IOA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author murphy
 */
@Configuration
@EnableWebSecurity
class PageSecurity extends WebSecurity {
	@Autowired
	IOA oa;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		super.configure(http);
		http.exceptionHandling()
			.accessDeniedHandler(new AccessDeniedHandlerImpl())
			.and()
			.authorizeRequests()
			.accessDecisionManager(new AccessImpl(oa.getPage(), Arrays.asList(AUTHORIZED_URL)));
	}

	public PageSecurity() {
		this.userProvider = username -> {
			StaffDTO staff = oa.getStaff(username);
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

@Slf4j(topic = "api")
class AccessDeniedHandlerImpl implements AccessDeniedHandler {
	/**
	 * 如果想要跳转页面，可以使用以下语句：
	 * request.setAttribute("msg", e.getMessage());
	 * request.getRequestDispatcher("/login/error").forward(request, response);
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(Json.toJsonString(new Result(-1, e.getMessage())));
	}
}

class AccessImpl implements AccessDecisionManager {
	private List<PageDTO> pages;
	private List<String> urls;

	public AccessImpl(List<PageDTO> pages, List<String> urls) {
		this.pages = pages;
		this.urls = new ArrayList(urls);
		this.urls.remove(0);
	}

	@Override
	public void decide(Authentication auth, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
		String url = ((FilterInvocation) o).getRequest().getRequestURI().toLowerCase();
		boolean authorized = urls.stream().anyMatch(u -> "/".equals(url) || url.startsWith(u.replace("**", "")));
		if (authorized) {
			return;
		}

		if (auth.getPrincipal() instanceof String) {
			throw new InsufficientAuthenticationException("您还没有登录。");
		} else {
			PageDTO p = pages.stream().filter(t -> t.getUrl().toLowerCase().equals(url)).findAny().orElse(null);
			if (Objects.nonNull(p)) {
				boolean present = auth.getAuthorities().stream().anyMatch(m -> m.getAuthority().equals("ROLE_" + p.getId()));
				if (!present) {
					throw new AccessDeniedException("您无权访问本页。");
				}
			} else {
				throw new AccessDeniedException("页面不存在。");
			}
		}
	}

	@Override
	public boolean supports(ConfigAttribute configAttribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return true;
	}
}