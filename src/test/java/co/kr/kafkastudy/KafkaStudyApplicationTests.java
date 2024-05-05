package co.kr.kafkastudy;

import co.kr.kafkastudy.api.service.RedisService;
import co.kr.kafkastudy.common.utils.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KafkaStudyApplicationTests {

    @Autowired
    private RedisService redisService;

    @Autowired
    private FileUtils fileUtils;

    @Value("${data-directory-path}")
    private String DATA_DIRECTORY_PATH;

    @Value("${fin-data-directory-path}")
    private String FIN_DATA_DIRECTORY_PATH;

    @Value("${fail-data-directory-path}")
    private String FAIL_DATA_DIRECTORY_PATH;

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

    @Test
    public void testFile() {
        System.out.println("START");
        fileUtils.moveFile(FIN_DATA_DIRECTORY_PATH+"sample.json", FAIL_DATA_DIRECTORY_PATH+"sample.json");
        System.out.println("END");
    }

}
