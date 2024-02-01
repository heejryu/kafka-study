package co.kr.kafkastudy.api.controller;

import co.kr.kafkastudy.api.service.KafkaTopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaTopicController {

    @Autowired
    private KafkaTopicService kafkaTopicService;


//    @GetMapping("/topic/create")
    public void test() {
        kafkaTopicService.createTopic("testtest", 3,3);
    }
}
