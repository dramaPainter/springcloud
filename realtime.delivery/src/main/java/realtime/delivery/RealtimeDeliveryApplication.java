package realtime.delivery;

import drama.painter.core.web.dal.DynamicDataSourceRegister;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import realtime.delivery.tool.Config;

/**
 * @author murphy
 */
@EnableEurekaClient
@SpringBootApplication
@Import(DynamicDataSourceRegister.class)
@MapperScan("web.mall.mapper")
@ComponentScan({"drama.painter.web.core", "realtime.delivery"})
public class RealtimeDeliveryApplication implements CommandLineRunner {
	@Autowired
	Config.PulsarClient consumer;

	@Autowired
	Config.ElasticSearchClient es;

	public static void main(String[] args) {
		SpringApplication.run(RealtimeDeliveryApplication.class, args);
	}

	@Override
	public void run(String... args) {
		//consumer.start(PulsarModel.getConsumerModel(es));
	}
}