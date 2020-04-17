package drama.painter.web.rbac.tool;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller增强器
 *
 * @author jim
 * @date 2017/11/23
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	/**
	 * 设置要捕获的异常，并作出处理
	 * 注意：这里可以返回试图，也可以放回JSON，这里就当做一个Controller使用
	 *
	 * @param request {@link HttpServletRequest}
	 * @param e       {@link Exception}
	 * @return {@link Map}
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Map handler(HttpServletRequest request, Exception e) {
		System.out.println("===========应用到所有@RequestMapping注解的方法，在其抛出Exception异常时执行");
		Map map = new HashMap(2);
		map.put("code", -1);
		map.put("message", e.getMessage());
		return map;
	}
}