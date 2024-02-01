package co.kr.kafkastudy.api.service;

import co.kr.kafkastudy.api.dao.CommonDAO;
import co.kr.kafkastudy.common.utils.RedisUtils;
import co.kr.kafkastudy.model.kafka.consumer.SongRecieve;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Repository
public class SongService {

    @Autowired
    private CommonDAO commonDAO;

    @Autowired
    private RedisUtils redisUtils;

    public void saveSongLylics(SongRecieve songRecieve){
        try {
            commonDAO.insert("song.saveSongLyrics", songRecieve);
        }catch (Exception e) {
            log.error("노래가사 저장 실패!!");
            e.printStackTrace();
        }
    }

    public void getSongFromRedis(String key){
//        redisUtils.
    }
}
