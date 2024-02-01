package co.kr.kafkastudy.model.redis;

import lombok.Data;

@Data
public class CreateKey {
    public String key;
    public String value;
    public Integer score;

    public CreateKey() {}

    public CreateKey(String key, String value, Integer score) {
        this.key = key;
        this.value = value;
        this.score = score;
    }
}
