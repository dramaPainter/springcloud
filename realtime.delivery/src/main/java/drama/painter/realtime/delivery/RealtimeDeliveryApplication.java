package drama.painter.realtime.delivery;

import drama.painter.core.web.dal.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author murphy
 */
@EnableEurekaClient
@SpringBootApplication
@Import(DynamicDataSourceRegister.class)
@ComponentScan({"drama.painter", "org.springframework.kafka.core"})
public class RealtimeDeliveryApplication {
	public static void main(String[] args) {
		SpringApplication.run(RealtimeDeliveryApplication.class, args);
	}
}