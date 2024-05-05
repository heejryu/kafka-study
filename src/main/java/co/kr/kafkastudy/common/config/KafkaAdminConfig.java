package co.kr.kafkastudy.common.config;

import co.kr.kafkastudy.constant.KafkaConstant;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaAdminConfig {
    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapAddress;


    @Bean
    public KafkaAdmin kafkaAdmin() {
        //When using Spring Boot, a KafkaAdmin bean is automatically registered
        // so you only need the NewTopic (and/or NewTopics) @Beans.
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfigurationProperties());
    }

    @Bean
    public NewTopic defaultTopic() {
        return TopicBuilder.name(KafkaConstant.DEFAULT_TOPIC)
                .partitions(3) //파티션수
                .replicas(1) //복제 개수
                .build();
    }
}
