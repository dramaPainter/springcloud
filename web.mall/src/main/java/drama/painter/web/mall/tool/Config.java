package drama.painter.web.mall.tool;

import drama.painter.core.web.config.ElasticSearch;
import drama.painter.core.web.config.Kafka;
import drama.painter.core.web.tool.HttpLog;
import drama.painter.core.web.tool.ftp.Upload;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.annotation.WebFilter;

/**
 * @author murphy
 */
@Component
public class Config {
	@Component
	public class ElasticSearchClient extends ElasticSearch {
	}

	@Component
	@WebFilter(urlPatterns = "*")
	class PostFilter extends HttpLog {
	}

	@Component
	public class Uploader extends Upload {
	}
}