package co.kr.kafkastudy.common.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class KafkaProducerComponent {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerComponent(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(ProducerRecord<String,Object> producerRecord) {

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(producerRecord);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
//                handleSuccess(producerRecord);
            }
            else {
//                handleFailure(producerRecord, ex);
            }
        });
    }
}
