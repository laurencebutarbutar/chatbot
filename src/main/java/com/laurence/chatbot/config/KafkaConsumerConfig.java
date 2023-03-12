package com.laurence.chatbot.config;

import com.laurence.chatbot.exception.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    private static final String MAX_CONCURRENT_CONSUMER = "max-concurrent-consumer";
    private static final String MAX_ATTEMPTS_RETRY = "max-attempts-retry";
    private static final String BACK_OFF_PERIOD = "back-off-period";

    private final KafkaProperties kafkaProperties;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setRetryTemplate(retryTemplate());
        Optional.ofNullable(getProperties(MAX_CONCURRENT_CONSUMER))
                .map(Integer::parseInt)
                .ifPresent(factory::setConcurrency);
        return factory;
    }

    private ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProperties());
    }

    private Map<String, Object> consumerProperties() {
        return kafkaProperties.getConsumer().buildProperties();
    }

    private RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(retryPolicy());
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy());
        return retryTemplate;
    }

    private RetryPolicy retryPolicy() {
        int maxAttempts = Optional.ofNullable(getProperties(MAX_ATTEMPTS_RETRY))
                .map(Integer::parseInt)
                .orElse(0);

        return new SimpleRetryPolicy(maxAttempts,
                Collections.singletonMap(RetryableException.class, true),
                true);
    }

    private FixedBackOffPolicy fixedBackOffPolicy() {
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        Optional.ofNullable(getProperties(BACK_OFF_PERIOD))
                .map(Long::parseLong)
                .ifPresent(fixedBackOffPolicy::setBackOffPeriod);
        return fixedBackOffPolicy;
    }

    private String getProperties(String key) {
        return kafkaProperties.getProperties().get(key);
    }
}
