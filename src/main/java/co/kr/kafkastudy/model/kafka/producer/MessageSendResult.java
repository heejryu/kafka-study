package co.kr.kafkastudy.model.kafka.producer;

import lombok.Data;

@Data
public class MessageSendResult {
    private String topicName;
    private int partition;
    private long offset;
    private boolean hasOffset;
    private String timestamp;
    private boolean hasTimestamp;
    private String statusCode;
    private String errorLog;

    public MessageSendResult() {}

    public MessageSendResult(String topicName, int partition, long offset, boolean hasOffset, String timestamp, boolean hasTimestamp) {
        this.topicName = topicName;
        this.partition = partition;
        this.offset = offset;
        this.hasOffset = hasOffset;
        this.timestamp = timestamp;
        this.hasTimestamp = hasTimestamp;
    }
}
