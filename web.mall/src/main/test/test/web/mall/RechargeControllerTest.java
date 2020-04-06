package test.web.mall;

import drama.painter.core.web.utility.Randoms;
import drama.painter.core.web.utility.Strings;
import drama.painter.web.mall.MallApplication;
import drama.painter.web.mall.service.inf.IRecharge;
import drama.painter.web.mall.service.pay.enums.MethodEnum;
import drama.painter.web.mall.service.pay.interfaces.IPayService;
import drama.painter.web.mall.service.pay.model.Exchange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@AutoConfigureMockMvc
@SpringBootTest(classes = MallApplication.class)
public class RechargeControllerTest {
	@Value("Basic #{T(org.apache.commons.codec.binary.Base64).encodeBase64String('${spring.cloud.config.username}:${spring.cloud.config.password}'.getBytes())}")
	String password;

	@Autowired
	MockMvc mvc;

	CountDownLatch latch = new CountDownLatch(100);

	@Autowired
	IRecharge recharge;

	@Autowired
	IPayService service;

	@Test
	public void unionpay() {
		Exchange one = Exchange.builder().account("6217231116000965680").address("广东省深圳市华强北支行").cash(10000)
			.ip("127.0.0.1").method(MethodEnum.UNIONPAY).orderid(Strings.getOrderId("1")).partner(11)
			.realname("刘子华").userid(80808080).fixdate("2020-02-22 15:31:22").build();
		service.exchange(one, "http://test.pay.ske666.com/callback/11");
	}

	@Test
	public void test() throws InterruptedException {
		ExecutorService pool = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 50; i++) {
			pool.execute(new Concurrent(latch, mvc, password, i));
		}


		System.out.println("开始任务 -------------");
		latch.await();
		System.out.println("------------- 任务完成");
	}

	public static class Concurrent implements Runnable {
		CountDownLatch latch;
		MockMvc mvc;
		String password;
		int id;

		public Concurrent(CountDownLatch latch, MockMvc mvc, String password, int id) {
			this.latch = latch;
			this.mvc = mvc;
			this.password = password;
			this.id = id;
		}

		@Override
		public void run() {
			latch.countDown();

			try {
				int rand = Integer.parseInt(Randoms.getRandomNumber(1));
				String r = mvc.perform(MockMvcRequestBuilders
					.post("/chat/update?id=1&quantity=" + rand)
					.header("Authorization", password))
					.andReturn().getResponse().getContentAsString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}