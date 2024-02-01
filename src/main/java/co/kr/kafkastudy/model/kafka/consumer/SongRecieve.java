package co.kr.kafkastudy.model.kafka.consumer;

import lombok.Data;

@Data
public class SongRecieve {
    private String songName;
    private Integer verse;
    private String lyrics;
    private Integer line;
    private boolean isEnd;
    private String date;
}
