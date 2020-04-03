package drama.painter.web.mall.service.impl;

import drama.painter.core.web.misc.Result;
import drama.painter.core.web.utility.Caches;
import drama.painter.core.web.utility.Randoms;
import drama.painter.web.mall.service.inf.IUpload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import drama.painter.web.mall.tool.Config;

import java.util.List;

/**
 * @author murphy
 */
@Slf4j(topic = "api")
@Service
public class UploadImpl implements IUpload {
	private static final String UPLOAD_TOKEN = "UPLOAD_TOKEN_";
	@Autowired
	Config.Uploader uploader;

	@Override
	public String getToken(String username) {
		String token = Randoms.getNonceString();
		Caches.add("UPLOAD_TOKEN_".concat(username), token, 5 * 60);
		return token;
	}

	@Override
	public Result upload(List<?> files, String username, long id, String token) {
		check(files, username, token);
		String filePath = "/image/association/group/{id%100}/{id}/{i}.png";
		return uploader.uploadList(files, filePath, id);
	}

	@Override
	public Result upload(String file, String username, long id, String token) {
		check(file, username, token);
		String filePath = "/image/chat/{yyyyMMdd}/{uuid}.png";
		return uploader.upload(file, filePath, id);
	}

	@Override
	public Result upload(MultipartFile file, String username, long id, String token) {
		check(file, username, token);
		String filePath = "/image/chat/{yyyyMMdd}/{uuid}.png";
		return uploader.upload(file, filePath, id);
	}

	static void check(Object files, String username, String token) {
		String key = UPLOAD_TOKEN.concat(username);
		String session = Caches.get(key);
		Caches.remove(key);
		if (session == null) {
			throw new RuntimeException("上传超时，请刷新页面");
		} else if (session == null || !session.equals(token)) {
			throw new RuntimeException("TOKEN不正确");
		} else if (files == null) {
			throw new RuntimeException("上传文件不能为空 Code:101");
		} else if (files instanceof List && ((List) files).size() == 0) {
			throw new RuntimeException("上传文件不能为空 Code:102");
		} else if (files instanceof String && files.toString().length() == 0) {
			throw new RuntimeException("上传文件不能为空 Code:103");
		}
	}
}
