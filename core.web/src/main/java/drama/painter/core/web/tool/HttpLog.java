package drama.painter.core.web.tool;

import drama.painter.core.web.utility.Dates;
import drama.painter.core.web.utility.Networks;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author murphy
 */
@Slf4j(topic = "http")
public class HttpLog implements Filter {
	static final String STATIC_FILE_PATH = "/file/";
	static final List<String> HEADER = Arrays.asList("NGINX-REMOTE-ADDRESS", "X-FORWARDED-FOR", "X-Real-IpTable");
	static final ThreadLocal<Long> TIME = new ThreadLocal();
	static final ThreadLocal<String> POST = new ThreadLocal();
	static final ThreadLocal<HttpServletRequest> REQUEST = new ThreadLocal();

	public static String getIp(HttpServletRequest request) {
		String name = HEADER.stream().filter(o -> request.getHeader(o) != null).findFirst().orElse(null);
		String value = name == null ? request.getRemoteAddr() : request.getHeader(name);
		return value == null ? "127.0.0.1" : (value.contains(",") ? value.split(",")[0] : value);
	}

	public static Map<String, String[]> getParameterMap() {
		return REQUEST.get().getParameterMap();
	}

	public static String getIp() {
		return getIp(REQUEST.get());
	}

	public static Long getTime() {
		return TIME.get();
	}

	public static String getPost() {
		if (POST.get() == null) {
			if (REQUEST.get() == null) {
				return "";
			} else {
				try {
					String post = Networks.post(REQUEST.get().getInputStream(), "utf-8");
					post = post == null ? "" : post;
					POST.set(post);
				} catch (Exception e) {
					log.error("无法设置当前request的POST值", e);
				}
			}
		}
		return POST.get();
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		servletResponse.setCharacterEncoding("utf-8");
		TIME.set(Dates.getNowLong());
		REQUEST.set((HttpServletRequest) servletRequest);
		try {
			filterChain.doFilter(servletRequest, servletResponse);
		} catch (IOException | ServletException e) {
			throw e;
		} finally {
			logHttp();
			TIME.remove();
			REQUEST.remove();
			POST.remove();
		}
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void destroy() {
	}

	void logHttp() {
		if (REQUEST.get().getRequestURI().contains(STATIC_FILE_PATH)) {
			return;
		}

		if (log.isDebugEnabled()) {
			String user = REQUEST.get().getUserPrincipal() == null ? "NULL" : REQUEST.get().getUserPrincipal().getName();
			String parameter = REQUEST.get().getParameterMap().entrySet().stream()
				.map(p -> p.getKey() + "=" + StringUtils.join(p.getValue()))
				.reduce((p1, p2) -> p1 + "&" + p2)
				.map(s -> "?" + s)
				.orElse("");
			log.debug("[{}] {} {} {} {}{}", user, System.currentTimeMillis() - TIME.get(), REQUEST.get().getRequestedSessionId(), getIp(REQUEST.get()), REQUEST.get().getRequestURL(), parameter);
		}
	}
}