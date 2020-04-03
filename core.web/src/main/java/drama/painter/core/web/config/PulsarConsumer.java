package drama.painter.core.web.config;

import drama.painter.core.web.utility.Dates;
import drama.painter.core.web.utility.Logs;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.shade.org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author murphy
 */
@Slf4j(topic = "pulsar")
public class PulsarConsumer {
	@Autowired
	PulsarClient pulsar;

	@Autowired
	ElasticSearch elasticSearch;

	static final int MAX_FAILED_TIMES = 100;
	static final int MONITOR_GAP = 10;
	static final BasicThreadFactory FACTORY = new BasicThreadFactory.Builder().namingPattern("RetryableConsumerThread").build();

	public void start(List<Pulsar.ConsumerModel> list) {
		final int size = list.size();
		ThreadPoolExecutor pool = new ThreadPoolExecutor(size, size, size, TimeUnit.SECONDS, new ArrayBlockingQueue(size), FACTORY);
		for (Pulsar.ConsumerModel model : list) {
			if (model.getType() == 1) {
				continue;
			}
			pool.execute(new ConsumerInstance(pulsar, model, elasticSearch));
		}
	}

	static class PulsarMessageListener<T> implements MessageListener<T> {
		ElasticSearch elasticSearch;
		Pulsar.IDataProccessor proccessor;

		public PulsarMessageListener(Pulsar.IDataProccessor proccessor, ElasticSearch elasticSearch) {
			this.proccessor = proccessor;
			this.elasticSearch = elasticSearch;
		}

		@Override
		public void received(Consumer<T> consumer, Message<T> msg) {
			proccessor.reply(consumer, msg);
		}
	}

	static class ConsumerInstance<T> implements Runnable {
		PulsarClient client;
		Pulsar.ConsumerModel<T> model;
		Consumer consumer;
		ElasticSearch elasticSearch;

		public ConsumerInstance(PulsarClient client, Pulsar.ConsumerModel<T> model, ElasticSearch elasticSearch) {
			this.client = client;
			this.model = model;
			this.elasticSearch = elasticSearch;
		}

		@SneakyThrows
		@Override
		public void run() {
			if (consumer == null) {
				consumer = addConsumer();
			} else if (consumer.getStats().getTotalReceivedFailed() > MAX_FAILED_TIMES) {
				if (!consumer.isConnected()) {
					consumer.close();
					consumer = addConsumer();
				}
			}
			Logs.sleep(MONITOR_GAP);
			run();
		}

		private <T> Consumer<T> addConsumer() throws PulsarClientException {
			return client.newConsumer(Pulsar.getSchema(model.getClazz()))
				.topic(model.getTopic())
				.subscriptionName(model.getSubscription())
				.consumerEventListener(new ConsumerEventListener() {
					@Override
					public void becameActive(Consumer<?> consumer, int i) {

					}

					@Override
					public void becameInactive(Consumer<?> consumer, int i) {

					}
				})
				.consumerName("消费者" + Dates.toDigitalTime(System.currentTimeMillis() / 1000))
				.subscriptionName("消费者")
				.subscriptionType(SubscriptionType.Exclusive)
				.ackTimeout(9, TimeUnit.SECONDS)
				.messageListener(new PulsarMessageListener(model.getProccessor(), elasticSearch))
				.subscribe();
		}

		public void recover() {
			consumer.resume();
		}

		public void pause() {
			consumer.pause();
		}
	}
}