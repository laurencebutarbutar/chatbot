package com.laurence.chatbot.config;

import com.laurence.chatbot.config.properties.SpringMailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class SpringMailConfig {

    private final SpringMailProperties springMailProperties;

    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(springMailProperties.getSender().getHost());
        mailSender.setPort(springMailProperties.getSender().getPort());
        mailSender.setUsername(springMailProperties.getSender().getUsername());
        mailSender.setPassword(springMailProperties.getSender().getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
