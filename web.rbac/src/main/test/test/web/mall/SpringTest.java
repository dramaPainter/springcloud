package test.web.mall;

import drama.painter.web.rbac.RbacApplication;
import drama.painter.web.rbac.service.IOa;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = RbacApplication.class)
public class SpringTest {
	@Autowired
	IOa oa;

	@Test
	public void test() {
		oa.listOperations(1, "2020-01-01 00:00:00,000","2020-09-01 00:00:00,000", 0,"web-rbac /login/login");
	}
}