package drama.painter.core.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;

/**
 * @author murphy
 */
public class Kafka {
	@Value("${kafka.server}")
	String servers;

	@Primary
	@Bean
	public KafkaProperties getKafkaProperties(KafkaProperties kafka) {
		kafka.setBootstrapServers(Arrays.asList(servers.split(",")));
		return kafka;
	}
}