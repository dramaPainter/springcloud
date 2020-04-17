package drama.painter.core.web.security;

import drama.painter.core.web.misc.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 使用FORM验证模式，登录成功后即可获得操作权限。
 * Web Security 执行顺序:
 * 第一步：执行retrieveUser方法。如果执行无误，没有后续步骤。
 * 第二步：执行loadUserByUsername方法。此方法只能抛出异常。
 * 第三步：再次回到retrieveUser方法。使用ThreadLocal避免重复读数据库。
 *
 * @author murphy
 */
public class LoginSecurityConfig extends WebSecurityConfigurerAdapter {
    static final String LOGIN_URL = "/login/login";
    static final String SECRET_KEY = "Web-Security-Client-Key";
    protected Function<String, User> userProvider;
    public static final String[] AUTHORIZED_URL = {"/", "/favicon.ico", "/file/**", "/login/**", "/actuator/**"};
    public static final List<String> AUTHORIZED_URL_LIST = Arrays.stream(AUTHORIZED_URL)
            .filter(o -> !"/".equals(o))
            .map(o -> o.replace("**", ""))
            .collect(Collectors.toList());

    @Autowired
    PasswordAuthImpl auth;

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
        http.headers().frameOptions().disable().and().csrf().disable()
                .authenticationProvider(auth).authorizeRequests()
                .antMatchers(AUTHORIZED_URL).permitAll().anyRequest().authenticated()
                .and().rememberMe().rememberMeServices(getRememberMeServices())
                .and().logout().logoutUrl("/login/logout").logoutSuccessUrl("/")
                .invalidateHttpSession(true).clearAuthentication(true)
                .and().formLogin().loginProcessingUrl("/login/security").loginPage(LOGIN_URL).defaultSuccessUrl("/")
                .successHandler(new SuccessHandler()).failureHandler(new FailureHandler())
                .permitAll();
    }

    TokenBasedRememberMeServices getRememberMeServices() {
        TokenBasedRememberMeServices services = new TokenBasedRememberMeServices(SECRET_KEY, auth);
        services.setCookieName("remember");
        services.setParameter("remember");
        services.setTokenValiditySeconds(24 * 3600);
        return services;
    }

    static class BadLoginException extends AuthenticationException {
        public BadLoginException(String msg) {
            super(msg);
        }
    }
}

