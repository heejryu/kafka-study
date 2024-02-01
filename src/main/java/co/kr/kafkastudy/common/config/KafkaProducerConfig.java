package co.kr.kafkastudy.common.config;

import co.kr.kafkastudy.common.interceptor.KafkaProducerInterceptor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapAddress;

    @Autowired
    private KafkaProducerInterceptor kafkaProducerInterceptor;


    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        // template을 생성하는데 디테일한 설정값을 넣어주는 producerFactory
        DefaultKafkaProducerFactory<String, Object> defaultKafkaProducerFactory
                = new DefaultKafkaProducerFactory<>(producerConfigs());
        return defaultKafkaProducerFactory;
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        /*멱등성 위한 필수 설정*/
        // producer transaction 을 구분하기 위한 pid
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "tx-"+UUID.randomUUID());
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_DOC, true); // (default true) 중복데이터 적재를 해결하기 위해 true
        props.put(ProducerConfig.ACKS_CONFIG, "all"); // (default all) Producer 가 요청을 보내고 Leader 가 Replication의 수신을 확인해야되는 개수

        // (default <=5) 한 번에 몇 개의 요청(Request)을 전송할 것인가를 결정. 1보다 크면 재시도 시점에 따라 메시지 순서가 바뀔 수 있음
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,1);

        props.put(ProducerConfig.RETRIES_CONFIG,2); // 프로듀서가 에러났을때 재시도할 횟수

        /* (default 1200000(2분)) 레코드를 send 하고 성공/실패를 결정하는 시간. 브로커로부터 ack를 받기 위해 대기하는 시간 옵션
         복구할 수 없는 에러가 발생하거나 재시도 횟수를 다 소모하게 되면 delivery.timeout.ms 설정시간보다 적어도 에러를 발생할 수 있다.
         deliver.timout.ms >= request.timeout.ms + linger.ms
         */
//        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG,1200000);
//        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 100) // (default 100) 실패한 요청에 대해 프로듀서가 재시도하기 전에 대기할 시간

        /* (default 0) batch.size가 도달하지 않으면 쌓여있는 메세지를 처리할 수 없음. size가 도달하지 않아도 시간에 도달하면 메세지 전송.
         브로커에게 주는 부하를 줄이기 위해 메세지 보내는데 있어 약간 딜레이를 주는 용도로도 사용할 수 있음.
         */
//        props.put(ProducerConfig.LINGER_MS_CONFIG,30000);
//        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG,300000); // (default 30.0000(30초)) 요청한 응답에 대해 Producer가 기다리는 최대시간. 해당시간동안 응답받지 못하면 그 시간동안 재시도
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16); // (default 16kb)메시지 용량이 정의된 size에 도달할 때까지 기다렸다가 send

        return props;
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        KafkaTemplate<String, Object> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        kafkaTemplate.setProducerInterceptor(kafkaProducerInterceptor);
        return kafkaTemplate;
    }

    @Bean
    public Producer<String, Object> producer() {
        return new KafkaProducer<String, Object>(producerConfigs());
    }
}
