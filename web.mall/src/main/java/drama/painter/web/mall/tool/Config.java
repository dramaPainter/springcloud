package drama.painter.web.mall.tool;

import drama.painter.core.web.config.ElasticSearch;
import drama.painter.core.web.config.Pulsar;
import drama.painter.core.web.config.PulsarProducer;
import drama.painter.core.web.config.RabbitMessageQueue;
import drama.painter.core.web.tool.HttpLog;
import drama.painter.core.web.tool.ftp.Upload;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
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
	public class PulsarInstance extends Pulsar {
	}

	@Component
	public class PulsarClient extends PulsarProducer {
	}

	@Component
	@WebFilter(urlPatterns = "*")
	class PostFilter extends HttpLog {
	}

	@Configuration
	public class RabbitClient extends RabbitMessageQueue {
		@Override
		@Bean
		public Queue queue() {
			return new Queue("bus-refresh-kefu");
		}
	}

	@Component
	public class Uploader extends Upload {
	}
}