package com.laurence.chatbot.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "module")
public class ModuleProperties {

    private Jwt jwt;
    private Chat chat;
    private Transaction transaction;

    @Data
    public static class Jwt {
        private long validToken;
        private String secretKey;
    }

    @Data
    public static class Chat {
        private String amountPattern;
        private String emailPattern;
    }

    @Data
    public static class Transaction {
        private String invoicePrefix;
        private String datePattern;
    }
}
