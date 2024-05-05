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
        props.put(ProducerConfig.ACKS_CONFIG, "all"); // (default all) Producer 가 요청을 보내고 Leader 가 Replication의 수신을 확인해야되는 개수

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
