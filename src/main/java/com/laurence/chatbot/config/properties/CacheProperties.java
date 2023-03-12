package com.laurence.chatbot.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Set;

@Data
@Configuration
@ConfigurationProperties(prefix = "cache")
public class CacheProperties {

    private Common common;
    private Duration expiryTime;

    @Data
    public static class Common {

        private Set<String> prohibitedPatterns;
    }

}
