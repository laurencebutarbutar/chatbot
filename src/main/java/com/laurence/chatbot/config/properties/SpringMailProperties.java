package com.laurence.chatbot.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring-mail")
public class SpringMailProperties {

    private Sender sender;
    private SMTP smtp;

    @Data
    public static class Sender {
        private String host;
        private Integer port;
        private String username;
        private String password;
        private String fromEmail;
        private String fromName;
        private String subject;
    }

    @Data
    public static class SMTP {
        private boolean auth;
        private boolean starttlsEnable;
        private boolean starttlsRequired;
        private Integer connectionTimeout;
        private Integer timeout;
        private Integer writeTimeout;
    }

}
