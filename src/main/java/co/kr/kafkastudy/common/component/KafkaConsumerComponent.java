package co.kr.kafkastudy.common.component;

import co.kr.kafkastudy.api.service.SongService;
import co.kr.kafkastudy.common.utils.RedisUtils;
import co.kr.kafkastudy.model.kafka.consumer.SongRecieve;
import co.kr.kafkastudy.model.redis.CreateKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
@Component
public class KafkaConsumerComponent {
    private CountDownLatch latch = new CountDownLatch(10);
    private List<SongRecieve> songs = new ArrayList<>();

    String TOPIC_NAME1 = "national_anthem";
    String TOPIC_NAME2 = "idol";

    @Autowired
    private SongService songService;

    @Autowired
    private RedisUtils redisUtils;

//    @KafkaListener(topics = "${spring.kafka.default-topic}", containerFactory = "defaultKafkaListenerContainerFactory")
    public void listenDefaultTopic(ConsumerRecord<String,Object> record) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            SongRecieve songRecieve = objectMapper.readValue(String.valueOf(record.value()) ,SongRecieve.class);

            // 메시지 키, 메시지 값에 대한 처리
            log.info(record.toString());
            songs.add(songRecieve);

            //row별 DB 저장
            songService.saveSongLylics(songRecieve);

            /*redis 저장*/
//            //redis 곡전체 저장
//            CreateKey createSong = new CreateKey("national_anthem", record.toString(), songRecieve.getLine());
//            log.info("createSong ::: "+createSong);
//            redisUtils.createKeyInfo("national_anthem", songRecieve.getLyrics(), createSong.getScore());
//            //redis 곡전체 조회
//            List<CreateKey> result = redisUtils.getKeyInfo("national_anthem");
//            log.info("result ::: "+result);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }
}
