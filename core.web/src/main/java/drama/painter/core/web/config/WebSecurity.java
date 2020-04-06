package drama.painter.core.web.config;

import drama.painter.core.web.misc.User;
import drama.painter.core.web.tool.HttpLog;
import drama.painter.core.web.utility.Encrypts;
import drama.painter.core.web.utility.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Web Security 执行顺序:
 * 第一步：执行retrieveUser方法。如果执行无误，没有后续步骤。
 * 第二步：执行loadUserByUsername方法。此方法只能抛出异常。
 * 第三步：再次回到retrieveUser方法。使用ThreadLocal避免重复读数据库。
 *
 * @author murphy
 */
public class WebSecurity extends WebSecurityConfigurerAdapter {
	public static final String LOGIN_URL = "/login/login";
	static final String SECRET_KEY = "Web-Security-Client-Key";
	static final String[] AUTHORIZED_URL = {"/favicon.ico", "/images/**", "/chat/receive", "/chat/error", "/actuator/bus-refresh"};
	protected Function<String, User> userProvider;

	@Autowired
	PasswordAuthorizer auth;

	@PostConstruct
	public void init() {
		auth.config(userProvider);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().disable();
		http.csrf().disable().authenticationProvider(auth).authorizeRequests()
			.antMatchers(AUTHORIZED_URL).permitAll().anyRequest().hasAuthority("page101")
			.and().rememberMe().rememberMeServices(getRememberMeServices())
			.and().logout().logoutUrl("/login/logout").logoutSuccessUrl("/")
			.invalidateHttpSession(true).clearAuthentication(true)
			.and().formLogin().loginProcessingUrl("/login/security").loginPage(LOGIN_URL).defaultSuccessUrl("/")
			.successHandler(new SuccessHandler()).failureHandler(new FailureHandler())
			.permitAll();
	}

	TokenBasedRememberMeServices getRememberMeServices() {
		TokenBasedRememberMeServices services = new TokenBasedRememberMeServices(SECRET_KEY, auth);
		services.setCookieName("remeber");
		services.setParameter("remeber");
		services.setTokenValiditySeconds(24 * 3600);
		return services;
	}

	public static class PageUserDetails implements UserDetails {
		List<GrantedAuthority> auth;
		User user;

		public PageUserDetails(User user) {
			this.user = user;
			if (Objects.nonNull(user.getPermission())) {
				auth = Arrays.asList(user.getPermission().split(",")).stream()
					.map(o -> new SimpleGrantedAuthority(o))
					.collect(Collectors.toList());
			}
		}

		public User getUser() {
			return user;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return auth;
		}

		@Override
		public String getPassword() {
			return null;
		}

		@Override
		public String getUsername() {
			return user.getUsername();
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
	}
}

@Component
class PasswordAuthorizer extends AbstractUserDetailsAuthenticationProvider implements UserDetailsService {
	Function<String, User> userProvider;
	static ThreadLocal<User> USER = new ThreadLocal();

	@Autowired
	HttpServletRequest request;

	public void config(Function<String, User> userProvider) {
		this.hideUserNotFoundExceptions = false;
		this.userProvider = userProvider;
	}

	protected static void destroy() {
		USER.remove();
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new WebSecurity.PageUserDetails(new User(0, username));
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails detail, UsernamePasswordAuthenticationToken token) throws AuthenticationException {
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken token) throws AuthenticationException {
		if (USER.get() == null) {
			USER.set(userProvider.apply(username));
		}

		User user = USER.get();
		String password = token.getCredentials().toString();
		if (user == null) {
			throw new BadLoginException("帐号不存在");
		} else if (!user.getPassword().equals(Encrypts.md5(Encrypts.md5(password) + user.getSalt()))) {
			throw new BadLoginException("帐号或密码错误");
		} else if (user.getStatus().byteValue() != 1) {
			throw new BadLoginException("帐号已被冻结");
		}

		if (!StringUtils.isEmpty(user.getIp())) {
			String ip = HttpLog.getIp(request);
			Arrays.asList(user.getIp().split(",")).stream()
				.filter(o -> o.equals(ip)).findAny()
				.orElseThrow(() -> new BadLoginException("IP不在白名单"));
		}
		user.setPassword(null);
		return new WebSecurity.PageUserDetails(user);
	}
}

@Slf4j(topic = "api")
class SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
		PasswordAuthorizer.destroy();
		String username = request.getParameter("username");
		log.info("[登录][{}][{}]登录成功", username, HttpLog.getIp(request));
		super.onAuthenticationSuccess(request, response, authentication);
	}
}

@Slf4j(topic = "api")
class FailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
		PasswordAuthorizer.destroy();
		String username = request.getParameter("username");
		String param = WebSecurity.LOGIN_URL.concat("?error=").concat(Strings.urlencode(exception.getMessage()));
		log.info("[登录][{}][{}]登录失败，原因是：{}", username, HttpLog.getIp(request), exception.getMessage());
		response.sendRedirect(param);
	}
}

class BadLoginException extends AuthenticationException {
	public BadLoginException(String msg) {
		super(msg);
	}
}