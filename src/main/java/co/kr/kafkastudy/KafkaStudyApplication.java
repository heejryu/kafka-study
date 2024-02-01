package co.kr.kafkastudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class KafkaStudyApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaStudyApplication.class, args);
    }

}
