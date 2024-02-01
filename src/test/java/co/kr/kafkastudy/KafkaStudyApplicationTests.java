package co.kr.kafkastudy;

import co.kr.kafkastudy.api.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KafkaStudyApplicationTests {

    @Autowired
    private RedisService redisService;

    @Test
    public void createRedis() {
        redisService.createKeyInfo();
    }

    @Test
    public void getRedis() {
        redisService.getRedis();
    }

    @Test
    public void deleteRedesKey() {
        redisService.deleteRedisKey();
    }

}
