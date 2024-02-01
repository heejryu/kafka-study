package co.kr.kafkastudy.model.kafka.producer;

import lombok.Data;

import java.util.List;

@Data
public class SongVerse {
    private List<String> lyrics;
}
