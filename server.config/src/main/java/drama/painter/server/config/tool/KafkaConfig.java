package drama.painter.server.config.tool;

import drama.painter.server.config.ConfigApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;

/**
 * @author murphy
 */
@Configuration
public class KafkaConfig {
	@Value("${spring.cloud.config.server.native.search-locations}")
	String url;

	@Primary
	@Bean
	public KafkaProperties getKafkaProperties(KafkaProperties kafka) {
		try {
			Yaml yaml = new Yaml();
			String resource = (url.contains("classpath:") ? ConfigApplication.LOCAL_PATH + "/profile" : url) + "/api-production.yml";
			try (InputStream input = new FileInputStream(resource)) {
				Properties prop = yaml.loadAs(input, Properties.class);
				String servers = ((Map) prop.get("kafka")).get("server").toString();
				kafka.setBootstrapServers(Arrays.asList(servers.split(",")));
				return kafka;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	public ConsumerFactory getConsumerFactory(KafkaProperties prop) {
		return new DefaultKafkaConsumerFactory(prop.buildConsumerProperties());
	}
}
