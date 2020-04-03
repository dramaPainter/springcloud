package drama.painter.web.mall.service.inf;

import drama.painter.core.web.misc.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author murphy
 */
public interface IUpload {
	/**
	 * 获得TOKEN
	 *
	 * @param username 帐号名
	 * @return
	 */
	String getToken(String username);

	/**
	 * 上传文件
	 *
	 * @param files    文件对象列表
	 * @param username 帐号名
	 * @param id       对应ID
	 * @param token    TOKEN值
	 * @return
	 */
	Result upload(List<?> files, String username, long id, String token);

	/**
	 * 上传文件
	 *
	 * @param file     base64图片字符串
	 * @param username 帐号名
	 * @param id       对应ID
	 * @param token    TOKEN值
	 * @return
	 */
	Result upload(String file, String username, long id, String token);

	/**
	 * 上传文件
	 *
	 * @param file     文件对象
	 * @param username 帐号名
	 * @param id       对应ID
	 * @param token    TOKEN值
	 * @return
	 */
	Result upload(MultipartFile file, String username, long id, String token);
}
