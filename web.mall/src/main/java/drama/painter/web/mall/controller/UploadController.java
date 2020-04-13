package drama.painter.web.mall.controller;

import drama.painter.core.web.config.WebSecurity;
import drama.painter.core.web.misc.Result;
import drama.painter.web.mall.service.inf.IChat;
import drama.painter.web.mall.service.inf.IUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author murphy
 */
@Controller
public class UploadController {
	@Autowired
	IUpload upload;

	@Autowired
	IChat chat;

	@Value("${ftp.domain}")
	String domain;

	@GetMapping("/upload/page")
	public String upload(Map<String, Object> map) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		map.put("token", upload.getToken(username));
		map.put("id", 1999);
		return "upload/page";
	}

	@ResponseBody
	@PostMapping(value = "/upload/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result files(@RequestPart("file") List<MultipartFile> file, int id, String sign) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return upload.upload(file, username, id, sign);
	}

	@ResponseBody
	@PostMapping(value = "/upload/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Result file(@RequestPart("file") MultipartFile file, String sign) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return upload.upload(file, username, 0, sign);
	}

	@ResponseBody
	@PostMapping("/upload/file")
	public Result images(@RequestParam("file") List<String> file, int id, String sign) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		return upload.upload(file, username, id, sign);
	}

	@ResponseBody
	@PostMapping("/upload/image")
	public Result image(String file, int receiver) {
		WebSecurity.PageUserDetails userDetails = (WebSecurity.PageUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String sign = upload.getToken(userDetails.getUsername());
		file = file.substring(file.indexOf("base64,") + 7);
		Result r = upload.upload(file, userDetails.getUsername(), 0, sign);
		chat.saveMessage(userDetails.getUser().getUserid(), receiver, domain + r.getData(), true, userDetails.getUsername());
		return r;
	}
}
