package drama.painter.web.mall;

import drama.painter.core.web.dal.DynamicDataSourceRegister;
import org.mybatis.spring.annotation.MapperScan;
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
@MapperScan({"web.mall.mapper", "web.mall.service.pay.mapper"})
@ComponentScan({"drama.painter.web.core", "web.mall"})
public class MallApplication {
	public static void main(String[] args) {
		SpringApplication.run(MallApplication.class, args);
	}
}