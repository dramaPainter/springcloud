package drama.painter.realtime.delivery;

import drama.painter.core.web.dal.DynamicDataSourceRegister;
import drama.painter.realtime.delivery.model.ChatPO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import drama.painter.realtime.delivery.tool.Config;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * @author murphy
 */
@EnableEurekaClient
@SpringBootApplication
@Import(DynamicDataSourceRegister.class)
public class RealtimeDeliveryApplication {
	public static void main(String[] args) {
		SpringApplication.run(RealtimeDeliveryApplication.class, args);
	}

	@KafkaListener(topics = "chat")
	public void listen(ConsumerRecord<String, ChatPO> record) {
		System.out.println("kafka的key: " + record.key());
		System.out.println("kafka的value: " + record.value().toString());
	}
}