package com.laurence.chatbot.service.queue;

import com.laurence.chatbot.common.helper.MapperHelper;
import com.laurence.chatbot.config.properties.KafkaProperties;
import com.laurence.chatbot.enums.ResponseEnum;
import com.laurence.chatbot.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public abstract class BasePublisher<T> {

    private final KafkaProperties kafkaProperties;
    private final KafkaTemplate kafkaTemplate;

    public abstract void publish(T request);

    protected String getTopic(String key) {
        return Optional.ofNullable(kafkaProperties.getTopic())
                .map(KafkaProperties.Topic::getPublisher)
                .map(publisher -> publisher.get(key))
                .orElseThrow(() -> {
                    String message = String.format(ResponseEnum.KAFKA_TOPIC_NOT_FOUND.getMessage(), key);
                    return new BusinessException(ResponseEnum.KAFKA_TOPIC_NOT_FOUND, message);
                });
    }

    protected void send(String topic, Object response) {
        String payload = MapperHelper.convert(response);
        log.info("#publish() with topic : {} and payload : {}", topic, payload);
        kafkaTemplate.send(topic, payload);
    }
}
