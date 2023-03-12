package com.laurence.chatbot.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

    private Topic topic;


    @Data
    public static class Topic {

        private Map<String, String> publisher;
        private Map<String, String> listener;
    }
}
