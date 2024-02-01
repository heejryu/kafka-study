package co.kr.kafkastudy.entity;

import co.kr.kafkastudy.model.kafka.producer.SongVerse;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Data
@RedisHash(value = "song", timeToLive = -1L)
public class SongSave {

    @Id
    private String songName;
    private List<SongVerse> verse;
    private String date;

    public SongSave(String songName, List<SongVerse> verse, String date) {
        this.songName = songName;
        this.verse = verse;
        this.date = date;
    }
}
