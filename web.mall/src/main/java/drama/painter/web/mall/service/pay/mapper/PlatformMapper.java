package drama.painter.web.mall.service.pay.mapper;

import drama.painter.web.mall.service.pay.model.Mail;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author murphy
 */
@Repository
public interface PlatformMapper {
	/**
	 * 添加邮件
	 *
	 * @param m 邮件对象
	 * @return
	 */
	@Options(useGeneratedKeys = true, keyProperty = "mailid")
	@Insert({"INSERT INTO game.mail(userid, title, content, gem, score, mailType, adminid, state, sendTime, expireTime) ",
		"SELECT #{m.userid}, #{m.title}, #{m.content}, 0, #{m.score}, 2, #{m.adminid}, 0, NOW(), DATE_ADD(NOW(), INTERVAL 1 MONTH)"})
	int sendMail(@Param("m") Mail m);
}
