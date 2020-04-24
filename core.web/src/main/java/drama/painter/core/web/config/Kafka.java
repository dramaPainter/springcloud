package drama.painter.core.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
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
	final KafkaProperties prop;
	final ConsumerFactory consumerFactory;
	final ProducerFactory producerFactory;

	public Kafka(KafkaProperties prop, ConsumerFactory consumerFactory, ProducerFactory producerFactory) {
		this.prop = prop;
		this.consumerFactory = consumerFactory;
		this.producerFactory = producerFactory;
	}


	@Bean(name = "kafkaConsumerManualFactory")
	public KafkaListenerContainerFactory<?> kafkaConsumerManualFactory() {
		ConcurrentKafkaListenerContainerFactory<String, ?> factory = new ConcurrentKafkaListenerContainerFactory();
		factory.setConsumerFactory(consumerFactory);
		factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
		return factory;
	}

	@Bean(name = "kafkaConsumerBatchFactory")
	public KafkaListenerContainerFactory<?> kafkaConsumerBatchFactory() {
		ConcurrentKafkaListenerContainerFactory<String, ?> factory = new ConcurrentKafkaListenerContainerFactory();
		factory.setConsumerFactory(consumerFactory);
		factory.setConcurrency(1);
		factory.setBatchListener(true);
		ContainerProperties prop = factory.getContainerProperties();
		prop.setPollTimeout(3000);
		prop.setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
		return factory;
	}

	@Bean
	KafkaTemplate getKafkaTemplate() {
		return new KafkaTemplate(producerFactory);
	}
}

@Component
class KafkaBean {
	@Value("${kafka.server}")
	String servers;

	final KafkaProperties prop;

	public KafkaBean(KafkaProperties prop) {
		this.prop = prop;
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