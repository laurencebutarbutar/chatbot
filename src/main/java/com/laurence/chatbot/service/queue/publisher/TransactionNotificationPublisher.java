package com.laurence.chatbot.service.queue.publisher;

import com.laurence.chatbot.config.properties.KafkaProperties;
import com.laurence.chatbot.module.transaction.response.TransactionResponse;
import com.laurence.chatbot.service.queue.BasePublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TransactionNotificationPublisher extends BasePublisher<TransactionResponse> {

    private static final String TRANSACTION_NOTIFICATION = "transaction-notification";

    public TransactionNotificationPublisher(KafkaProperties kafkaProperties, KafkaTemplate kafkaTemplate) {
        super(kafkaProperties, kafkaTemplate);
    }

    @Override
    public void publish(TransactionResponse request) {
        String topic = getTopic(TRANSACTION_NOTIFICATION);
        if (StringUtils.isNotBlank(request.getCustomerEmail())) {
            send(topic, request);
        }

    }
}
