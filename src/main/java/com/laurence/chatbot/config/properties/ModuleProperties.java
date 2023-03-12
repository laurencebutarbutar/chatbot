package com.laurence.chatbot.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "module")
public class ModuleProperties {

    private Jwt jwt;

    @Data
    public static class Jwt {
        private long validToken;
        private String secretKey;
    }

}
