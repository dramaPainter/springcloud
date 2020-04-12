package drama.painter.web.rbac;

import drama.painter.core.web.dal.DynamicDataSourceRegister;
import drama.painter.web.rbac.tool.GlobalExceptionHandler;
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
@SpringBootApplication(scanBasePackages = "drama.painter")
@Import(DynamicDataSourceRegister.class)
@MapperScan("drama.painter.web.rbac.mapper")
@ComponentScan("drama.painter")
public class RbacApplication {
	public static void main(String[] args) {
		SpringApplication.run(RbacApplication.class, args);
	}
}