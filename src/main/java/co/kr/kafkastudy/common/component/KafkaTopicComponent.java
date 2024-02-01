package co.kr.kafkastudy.common.component;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.config.TopicConfig;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class KafkaTopicComponent {

    @Autowired
    private AdminClient adminClient;

    public void createTopic(String topicName , Integer partitions, Integer replicas) {
        try{
            //파티션이 0개면 토픽이 만들어지지않음
            List<NewTopic> topicsToAdd = new ArrayList<>();
            topicsToAdd.add(TopicBuilder.name(topicName)
                    .partitions(partitions) // 파티션 수
                    .replicas(replicas) //레플리카 수
                    .build());
            CreateTopicsResult topicResults = adminClient.createTopics(topicsToAdd);
            topicResults.all().get();
        }catch (ExecutionException ee){
            if(ee.getCause().getClass().equals(TopicExistsException.class)){
                log.warn("topicResults.allget() 이미 존재하는 토픽 : {}" , topicName);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void removeTopic(String topicName) {
        try{
            Map<String, TopicListing> topics = adminClient.listTopics().namesToListings().get();
            topics.entrySet().stream()
                    .map(Map.Entry::getKey)
                    .filter(topic -> topic.equals(topicName))
                    .peek(entry -> log.info("Deleting topic: {}", entry))
                    .forEach(topic -> adminClient.deleteTopics(Collections.singleton(topic)));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getTopics() {
        try{
            Map<String, TopicListing> topics = adminClient.listTopics().namesToListings().get();
            log.info("topics ? : {}" , topics);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getTopic(String topicName) {
        try{
            log.info("topicName ? : {}" , topicName);
            Map<String, TopicDescription> results = new HashMap<>();
            DescribeTopicsResult topics = adminClient.describeTopics(Arrays.asList(topicName));
            log.info("topics ? {}" , topics );
            results.putAll(topics.allTopicNames().get());
            log.info("results ? : {}" , results);
        }catch (ExecutionException ee){
            if(ee.getCause().getClass().equals(UnknownTopicOrPartitionException.class)){
                log.info("없는 토픽 : {}" , topicName);
            }else{
                ee.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
