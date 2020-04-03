package drama.painter.server.config.tool;

import drama.painter.server.config.ConfigApplication;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * @author murphy
 * 此类不能使用@Scope 因为Config Server本身不能调用自已刷新
 */
@Configuration
class RabbitMq {
	@Value("${spring.cloud.config.server.native.search-locations}")
	String url;

	@Bean
	public Queue myQueue() {
		return new Queue("bus-refresh-queue");
	}

	@Bean
	public CachingConnectionFactory connectionFactory() {
		try {
			Yaml yaml = new Yaml();
			String resource = (url.contains("classpath:") ? ConfigApplication.LOCAL_PATH + "/profile" : url) + "/api-production.yml";
			try (InputStream input = new FileInputStream(resource)) {
				Properties prop = yaml.loadAs(input, Properties.class);
				Map<String, String> map = (Map<String, String>) prop.get("rabbitmq");
				CachingConnectionFactory connection = new CachingConnectionFactory();
				connection.setHost(map.get("host"));
				connection.setUsername(map.get("username"));
				connection.setPassword(Decrypt.decrypt(map.get("password")));
				connection.setPort(Integer.parseInt(String.valueOf(map.get("port"))));
				prop.clear();
				map.clear();
				return connection;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	public AmqpAdmin amqpAdmin() {
		return new RabbitAdmin(connectionFactory());
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		return new RabbitTemplate(connectionFactory());
	}
}