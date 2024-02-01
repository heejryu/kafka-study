package co.kr.kafkastudy.api.service;

import co.kr.kafkastudy.common.utils.RedisUtils;
import co.kr.kafkastudy.model.kafka.producer.Song;
import co.kr.kafkastudy.model.kafka.producer.SongSend;
import co.kr.kafkastudy.model.kafka.producer.SongVerse;
import co.kr.kafkastudy.model.redis.CreateKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.List;

@Slf4j
@Repository
public class RedisService {
    @Autowired
    private RedisUtils redisUtils;

    @Value("${data-directory-path}")
    private String DATA_DIRECTORY_PATH;

    public void createKeyInfo(){
        ObjectMapper objectMapper = new ObjectMapper();
        File directory = new File(DATA_DIRECTORY_PATH);
        String[] files = directory.list();

        try {
            if (files != null) {
                if (files.length > 0) {
                    log.info("1. file size : {}, 목록 조회 완료.", files.length);

                    for (String fileName : files) {
                        log.info("2. file data 조회");

                        Song song = objectMapper.readValue(new File(DATA_DIRECTORY_PATH + fileName), Song.class);
                        String topicName = fileName.substring(0, fileName.indexOf("."));


                        for (int i = 0; i < song.getVerse().size(); i++) {
                            SongVerse verse = song.getVerse().get(i);

                            for (int k = 0; k < verse.getLyrics().size(); k++) {
                                SongSend songSend = new SongSend();
                                songSend.setSongName(topicName);
                                songSend.setVerse(i + 1);
                                songSend.setLyrics(verse.getLyrics().get(k));
                                songSend.setLine(k);
                                if (k == verse.getLyrics().size() - 1) {
                                    songSend.setIsEnd(true);
                                } else {
                                    songSend.setIsEnd(false);
                                }

                                //redis 곡전체 저장
                                CreateKey createSong = new CreateKey("national_anthem", songSend.getLyrics(), Integer.parseInt(String.valueOf(i)+String.valueOf(k)));
                                log.info("createSong ::: " + createSong);
                                redisUtils.createKeyInfo("national_anthem", songSend.getLyrics(), createSong.getScore());
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
                e.printStackTrace();
            }
        }

    public void getRedis() {
        List<CreateKey> result = redisUtils.getKeyInfo("national_anthem");
        log.info("result ::: "+result);
    }

    public void deleteRedisKey() {
        boolean isDelete = redisUtils.deleteKey("national_anthem");
        log.info("isDelete ::: "+isDelete);
    }

}
