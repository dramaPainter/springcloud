package drama.painter.core.web.misc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author murphy
 */
@Data
@NoArgsConstructor
public class User {
    public User(Integer userid, String username) {
        this.userid = userid;
        this.username = username;
    }

    Integer userid;
    String username;
    String nickname;
    String headimage;
    Byte status;

    @JsonIgnore
    String password;
    @JsonIgnore
    String salt;
    @JsonIgnore
    String ip;
    @JsonIgnore
    String permission;
}
