package co.kr.kafkastudy.common.component;

import co.kr.kafkastudy.api.service.KafkaService;
import co.kr.kafkastudy.common.utils.FileUtils;
import co.kr.kafkastudy.model.kafka.producer.Song;
import co.kr.kafkastudy.model.kafka.producer.SongSend;
import co.kr.kafkastudy.model.kafka.producer.SongVerse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ScheduledTaskComponent {

    @Value("${data-directory-path}")
    private String DATA_DIRECTORY_PATH;

    @Value("${fin-data-directory-path}")
    private String FIN_DATA_DIRECTORY_PATH;

    @Value("${fail-data-directory-path}")
    private String FAIL_DATA_DIRECTORY_PATH;

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private FileUtils fileUtils;

    @Scheduled(fixedRate = 5000)
    public void checkAndSendTopicData() throws Exception{
        boolean isFinFileMove = false;
        File directory = new File(DATA_DIRECTORY_PATH);
        String[] files = directory.list();
        if (files != null) {
            if (files.length > 0) {
                log.info("1. file size : {} 개, 목록 조회 완료.", files.length);
                for (String fileName : files) {
                    kafkaService.sendSongMessage(DATA_DIRECTORY_PATH, fileName);
                    isFinFileMove = fileUtils.moveFile(DATA_DIRECTORY_PATH+fileName, FIN_DATA_DIRECTORY_PATH+"20240130_"+fileName);

                    if (isFinFileMove){
                        log.info("3. File data publish 후 fin 폴더로 이동 SUCCESS");
                    }else{
                        fileUtils.moveFile(FIN_DATA_DIRECTORY_PATH+"20240130_"+fileName, FAIL_DATA_DIRECTORY_PATH+"20240130_"+fileName);
                        log.error("3. File data publish 후 fin 폴더로 이동 FAIL, fail 폴더로 이동");
                    }
                }
            }else {
                log.info("directory 내 파일없음");
            }
        }else {
            log.info("directory 없음");
        }
    }
}
