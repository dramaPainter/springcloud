package drama.painter.core.web.misc;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author murphy
 */
@NoArgsConstructor
@Data
public class User {
	public User(Integer userid, String username) {
		this.userid = userid;
		this.username = username;
	}

	Integer userid;
	String username;
	String password;
	String salt;
	Byte status;
	String ip;
	String permission;
}
