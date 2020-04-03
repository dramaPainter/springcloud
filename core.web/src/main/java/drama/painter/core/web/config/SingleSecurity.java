package drama.painter.core.web.config;

import drama.painter.core.web.utility.Encrypts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author murphy
 */
public class SingleSecurity extends WebSecurityConfigurerAdapter {
	@Value("${spring.cloud.config.username}")
	String username;

	@Value("${spring.cloud.config.password}")
	String password;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().sameOrigin();
		http.authorizeRequests().antMatchers("/**").authenticated().and().httpBasic().and().csrf().disable();
	}

	@Bean
	protected PasswordEncoder passwordEncoder() {
		return new NativePasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser(username).password(password).roles("LOGIN");
	}
}

class NativePasswordEncoder implements PasswordEncoder {
	@Override
	public boolean matches(CharSequence raw, String encoded) {
		return encoded.equals(raw);
	}

	@Override
	public String encode(CharSequence password) {
		return Encrypts.encrypt((String) password);
	}
}