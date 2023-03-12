package com.laurence.chatbot.service.queue.listener;

import com.laurence.chatbot.common.helper.MapperHelper;
import com.laurence.chatbot.config.properties.KafkaProperties;
import com.laurence.chatbot.enums.TransactionStatus;
import com.laurence.chatbot.module.transaction.response.TransactionResponse;
import com.laurence.chatbot.service.NotificationService;
import com.laurence.chatbot.service.queue.BaseListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionNotificationListener extends BaseListener<TransactionResponse> {

    private static final String TRANSACTION_NOTIFICATION = "transaction-notification";

    private final KafkaProperties kafkaProperties;
    private final NotificationService notificationService;

    public TransactionNotificationListener(KafkaProperties kafkaProperties, NotificationService notificationService) {
        this.kafkaProperties = kafkaProperties;
        this.notificationService = notificationService;
    }


    @KafkaListener(topics = "${kafka.topic.listener.transaction-notification}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(@Payload String payload) {
        String topic = kafkaProperties.getTopic().getListener().get(TRANSACTION_NOTIFICATION);
        TransactionResponse request = MapperHelper.convert(payload, getPayloadType());
        if (isValidTransaction(request)) {
            consume(topic, payload);
        }
    }

    @Override
    protected Class<TransactionResponse> getPayloadType() {
        return TransactionResponse.class;
    }

    @Override
    protected void process(TransactionResponse request) {
        if (isValidTransaction(request)) {
            notificationService.sendEmail(request);
        }
    }

    private boolean isValidTransaction(TransactionResponse request) {
        if(TransactionStatus.SUCCESS.name().equals(request.getStatus()) && ObjectUtils.isNotEmpty(request.getCustomerEmail())){
            return true;
        }
        return false;
    }
}
