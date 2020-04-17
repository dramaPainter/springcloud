package drama.painter.core.web.security;

import drama.painter.core.web.misc.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author murphy
 */
public class PageUserDetails implements UserDetails {
    List<GrantedAuthority> auth;
    final User user;

    public PageUserDetails(User user) {
        this.user = user;
        this.auth = StringUtils.isEmpty(user.getPermission()) ? Collections.emptyList() :
                Arrays.stream(user.getPermission().split(","))
                        .map(o -> new SimpleGrantedAuthority("ROLE_" + o))
                        .collect(Collectors.toList());
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
