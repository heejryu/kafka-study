package co.kr.kafkastudy.model.kafka.producer;

import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

@Data
public class SongSend {
    private String songName;
    private Integer verse;
    private String lyrics;
    private Integer line;
    private Boolean isEnd;
    private String date;
}
