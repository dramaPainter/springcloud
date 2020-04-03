package drama.painter.core.web.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.pulsar.client.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author murphy
 */
public class Pulsar {
	@Autowired
	PulsarConfig config;

	static final Object LOCKER = new Object[0];
	static final Map<Class, Schema> SCHEMA_MAP = new HashMap();

	public static <T> Schema<T> getSchema(Class<T> clazz) {
		Schema<T> schema = SCHEMA_MAP.get(clazz);
		if (schema == null) {
			synchronized (LOCKER) {
				if (schema == null) {
					schema = Schema.JSON(clazz);
					SCHEMA_MAP.put(clazz, schema);
				}
			}
		}
		return schema;
	}

	@Bean
	public PulsarClient getClient() throws PulsarClientException {
		return PulsarClient.builder().serviceUrl(config.getUrl()).build();
	}

	public interface IDataProccessor {
		/**
		 * 处理每个消息
		 *
		 * @param consumer Consumer对象
		 * @param msg      消息对象
		 * @param <T>      消息类型
		 */
		<T> void reply(Consumer<T> consumer, Message<T> msg);
	}

	@AllArgsConstructor
	@Data
	public static class ConsumerModel<T> {
		String subscription;
		String topic;
		int type;
		Class<T> clazz;
		Pulsar.IDataProccessor proccessor;
	}
}

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "pulsar")
class PulsarConfig {
	String url;
}