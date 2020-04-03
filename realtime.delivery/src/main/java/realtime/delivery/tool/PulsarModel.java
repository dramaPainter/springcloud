package realtime.delivery.tool;

import drama.painter.core.web.config.Pulsar;
import drama.painter.core.web.pulsar.zero.ChatPO;
import realtime.delivery.service.impl.ChatProccessorImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author murphy
 */
public class PulsarModel {
	public static List<Pulsar.ConsumerModel> getConsumerModel(Config.ElasticSearchClient es) {
		List<Pulsar.ConsumerModel> list = new ArrayList();
		list.add(new Pulsar.ConsumerModel("zero.chat", "persistent://web/zero/chat", 1, ChatPO.class, new ChatProccessorImpl(es)));
		return list;
	}
}