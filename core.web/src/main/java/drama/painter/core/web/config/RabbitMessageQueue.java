package drama.painter.core.web.config;

import drama.painter.core.web.utility.Encrypts;
import lombok.Data;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author murphy
 */
public class RabbitMessageQueue {
	@Autowired
	RabbitConfig config;

	@Bean
	public Queue queue() {
		return new Queue("bus-refresh-queue");
	}

	@Bean
	public CachingConnectionFactory connectionFactory() {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(config.getHost(), config.getPort());
		cachingConnectionFactory.setUsername(config.getUsername());
		cachingConnectionFactory.setPassword(Encrypts.decrypt(config.getPassword()));
		return cachingConnectionFactory;
	}

	@Bean
	public AmqpAdmin amqpAdmin() {
		return new RabbitAdmin(connectionFactory());
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		return new RabbitTemplate(connectionFactory());
	}

	@RefreshScope
	@Data
	@Component
	@Configuration
	@ConfigurationProperties(prefix = "rabbitmq")
	public static class RabbitConfig {
		private String host;
		private String username;
		private String password;
		private int port;
	}
}