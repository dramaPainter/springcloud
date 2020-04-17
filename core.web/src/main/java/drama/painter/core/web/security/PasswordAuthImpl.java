package drama.painter.core.web.security;

import drama.painter.core.web.config.HttpLog;
import drama.painter.core.web.misc.User;
import drama.painter.core.web.utility.Encrypts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.function.Function;

/**
 * @author murphy
 */
@Component
class PasswordAuthImpl extends AbstractUserDetailsAuthenticationProvider implements UserDetailsService {
    Function<String, User> userProvider;
    final static ThreadLocal<User> USER = new ThreadLocal();
    final HttpServletRequest request;

    public PasswordAuthImpl(HttpServletRequest request) {
        this.request = request;
    }

    public void config(Function<String, User> userProvider) {
        this.hideUserNotFoundExceptions = false;
        this.userProvider = userProvider;
    }

    protected static void destroy() {
        USER.remove();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new PageUserDetails(new User(0, username));
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
            throw new LoginSecurityConfig.BadLoginException("帐号不存在");
        } else if (!user.getPassword().equals(Encrypts.md5(Encrypts.md5(password) + user.getSalt()))) {
            throw new LoginSecurityConfig.BadLoginException("帐号或密码错误");
        } else if (user.getStatus().byteValue() != 1) {
            throw new LoginSecurityConfig.BadLoginException("帐号已被冻结");
        }

        if (!StringUtils.isEmpty(user.getIp())) {
            if (StringUtils.isEmpty(user.getIp())) {
                String ip = HttpLog.getIp(request);
                Arrays.stream(user.getIp().split(","))
                        .filter(o -> o.equals(ip)).findAny()
                        .orElseThrow(() -> new LoginSecurityConfig.BadLoginException("IP不在白名单"));
            }
        }
        user.setPassword(null);
        return new PageUserDetails(user);
    }
}
