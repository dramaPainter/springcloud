package drama.painter.core.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author murphy
 */
@Component
public class Kafka {
	@Value("${kafka.server}")
	String servers;

	@Autowired
	KafkaProperties prop;

	@Bean(name = "kafkaConsumerManualFactory")
	public KafkaListenerContainerFactory<?> kafkaConsumerManualFactory() {
		ConcurrentKafkaListenerContainerFactory<String, ?> factory = new ConcurrentKafkaListenerContainerFactory();
		factory.setConsumerFactory(kafkaConsumerFactory());
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
		return factory;
	}

	@Bean(name = "kafkaConsumerBatchFactory")
	public KafkaListenerContainerFactory<?> kafkaConsumerBatchFactory() {
		ConcurrentKafkaListenerContainerFactory<String, ?> factory = new ConcurrentKafkaListenerContainerFactory();
		factory.setConsumerFactory(kafkaConsumerFactory());
		factory.setConcurrency(1);
		factory.setBatchListener(true);
		ContainerProperties prop = factory.getContainerProperties();
		prop.setPollTimeout(3000);
		prop.setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
		return factory;
	}

	@Bean
	public ProducerFactory kafkaProducerFactory() {
		prop.setBootstrapServers(Arrays.asList(servers.split(",")));
		DefaultKafkaProducerFactory factory = new DefaultKafkaProducerFactory(prop.buildProducerProperties());
		factory.setValueSerializer(new JsonSerializer());
		return factory;
	}

	@Bean
	public ConsumerFactory kafkaConsumerFactory() {
		prop.setBootstrapServers(Arrays.asList(servers.split(",")));
		DefaultKafkaConsumerFactory factory = new DefaultKafkaConsumerFactory(prop.buildConsumerProperties());
		JsonDeserializer json = new JsonDeserializer();
		json.addTrustedPackages("*");
		factory.setValueDeserializer(json);
		return factory;
	}
}