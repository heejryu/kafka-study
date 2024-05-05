package co.kr.kafkastudy.api.service;

import co.kr.kafkastudy.api.dao.CommonDAO;
import co.kr.kafkastudy.common.component.KafkaProducerComponent;
import co.kr.kafkastudy.common.utils.DateUtils;
import co.kr.kafkastudy.constant.KafkaConstant;
import co.kr.kafkastudy.model.kafka.producer.MessageSendResult;
import co.kr.kafkastudy.model.kafka.producer.Song;
import co.kr.kafkastudy.model.kafka.producer.SongSend;
import co.kr.kafkastudy.model.kafka.producer.SongVerse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

@Slf4j
@Repository
public class KafkaService {
    @Autowired
    private CommonDAO commonDao;

    @Autowired
    private KafkaProducerComponent kafkaProducerComponent;

    @Autowired
    private DateUtils dateUtils;

    public void sendSongMessage(String filePath, String fileName) {
        File dataFile = new File(filePath+fileName);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Song song = objectMapper.readValue(dataFile, Song.class);
            String song_name = fileName.substring(0,fileName.indexOf("."));

            for (int i = 0; i < song.getVerse().size(); i++) {
                SongVerse verse = song.getVerse().get(i);

                for (int k = 0; k < verse.getLyrics().size(); k++) {
                    SongSend songSend = new SongSend();
                    songSend.setSongName(song_name);
                    songSend.setVerse(i+1);
                    songSend.setLyrics(verse.getLyrics().get(k));
                    songSend.setLine(k);
                    songSend.setIsEnd(k == verse.getLyrics().size() - 1);

                    ProducerRecord<String, Object> producerRecord
                            = new ProducerRecord<>(KafkaConstant.DEFAULT_TOPIC, i, "verse"+i, songSend);
                    kafkaProducerComponent.sendMessage(producerRecord);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void createMessageSendResultLog(MessageSendResult messageSendResult) {
        try {
            commonDao.insert("producer.messageSendResultLog",messageSendResult);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
