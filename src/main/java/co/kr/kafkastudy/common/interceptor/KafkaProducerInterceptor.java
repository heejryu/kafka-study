package co.kr.kafkastudy.common.interceptor;

import co.kr.kafkastudy.api.service.KafkaService;
import co.kr.kafkastudy.common.utils.DateUtils;
import co.kr.kafkastudy.constant.DateConstant;
import co.kr.kafkastudy.constant.KafkaConstant;
import co.kr.kafkastudy.model.kafka.producer.MessageSendResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.datatype.DatatypeConstants;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class KafkaProducerInterceptor implements ProducerInterceptor<String, Object> {

    @Autowired
    private DateUtils dateUtils;

    private Producer<String, String> producer;

    @Override
    public ProducerRecord<String, Object> onSend(ProducerRecord<String, Object> record) {
        //메시지를 전송하기 전에 호출 호출
        // record를 수정하여 반환?
        log.info("[MESSAGE BeforeSend] topic, message header -> {}, {}", record.topic(), record.headers());
        log.info("[MESSAGE BeforeSend] message body -> {}", record.value());
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        //레코드가 성공적으로 전송되면 호출, 실패시 커밋실패
        // metadata: 성공한 레코드에 대한 메타데이터, exception: 전송 중 발생한 예외 (성공 시 null)
        MessageSendResult messageSendResult = new MessageSendResult(
                metadata.topic()
                ,metadata.partition()
                ,metadata.offset()
                ,metadata.hasOffset()
                ,dateUtils.timestampToDateTime(metadata.timestamp(), DateConstant.BASE_DATE_PATTERN)
                ,metadata.hasTimestamp()
        );

        if (exception != null) {
            log.error("[MESSAGE AfterSend] exception :"+ exception);
            messageSendResult.setStatusCode(KafkaConstant.PRODUCER_MESSAGE_SEND_FAIL);
            messageSendResult.setErrorLog(String.valueOf(exception));
            //TODO 결과 데이터 저장
        }

        log.info("[MESSAGE AfterSend] ::: "+messageSendResult);
    }

    @Override
    public void close() {
        //인터셉터를 닫을 때 호출되는 메소드
        if (producer != null) {
            producer.close();
        }
    }

    @Override
    public void configure(Map<String, ?> configs) {
        // 인터셉터를 설정할 때 호출되는 메소드
    }
}
