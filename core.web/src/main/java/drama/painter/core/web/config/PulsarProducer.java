package drama.painter.core.web.config;

import drama.painter.core.web.utility.Dates;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.ProducerBuilder;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author murphy
 */
@Slf4j(topic = "pulsar")
public class PulsarProducer {
	@Autowired
	PulsarClient client;

	public <T> void send(String topic, String key, T data, Class<T> clazz) {
		ProducerBuilder<T> builder = client.newProducer(Pulsar.getSchema(clazz))
			.topic(topic)
			.producerName("生产者" + Dates.toDigitalTime(System.currentTimeMillis() / 1000))
			.sendTimeout(10, TimeUnit.SECONDS)
			.blockIfQueueFull(true);

		Producer<T> producer = null;
		try {
			producer = builder.create();
			CompletableFuture<Void> future = producer.newMessage()
				.key(key)
				.value(data)
				.sendAsync()
				.thenAcceptAsync(msgid -> PulsarProducer.log.info("[生产]消息发送成功：{}", data));
			future.join();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (producer != null) {
				producer.closeAsync();
			}
		}
	}
}
