package drama.painter.realtime.delivery.tool;

import drama.painter.realtime.delivery.model.ChatPO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.stereotype.Service;

/**
 * @author murphy
 */
@Slf4j(topic = "api")
@Service
public class KafkaReceiver extends DefaultJackson2JavaTypeMapper {
    final Config.ElasticSearchClient es;

    public KafkaReceiver(Config.ElasticSearchClient es) {
        this.es = es;
    }

    /**
     * containerFactory 单个接收 kafkaConsumerManualFactory 批量接收 kafkaConsumerBatchFactory 这两个名字都来自Kafka的配置类
     *
     * @param record 单个接收对象 ConsumerRecord＜String, ChatPO＞ 批量接收对象 List＜ConsumerRecord＜String, ChatPO＞＞
     * @param ack    应答对象
     */
    @KafkaListener(topics = "chat", groupId = "聊天系统", containerFactory = "kafkaConsumerManualFactory")
    public void kafkaReceived(ConsumerRecord<String, ChatPO> record, Acknowledgment ack) {
        log.info("收到消息 SINGLE  ====  {}", record);
        ack.acknowledge();
    }
}
