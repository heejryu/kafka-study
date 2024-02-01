package co.kr.kafkastudy.model.kafka.producer;

import lombok.Data;

import java.util.List;

@Data
public class Song {
    private String songName;
    private List<SongVerse> verse;
    private String date;
}
