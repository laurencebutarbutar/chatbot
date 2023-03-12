package com.laurence.chatbot.service;

import com.laurence.chatbot.config.properties.SpringMailProperties;
import com.laurence.chatbot.entity.mongo.Email;
import com.laurence.chatbot.enums.TransactionStatus;
import com.laurence.chatbot.module.transaction.response.TransactionResponse;
import com.laurence.chatbot.repository.mongo.EmailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    @Autowired
    private JavaMailSender javaMailSender;

    private final SpringMailProperties springMailProperties;
    private final EmailRepository emailRepository;

    public void sendEmail(TransactionResponse request) {

        try {
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            Email emailTemplate = emailRepository.findFirstByStatus(TransactionStatus.SUCCESS.name());
            mimeBodyPart.setText(emailTemplate.getTemplate(), "utf-8", "html");

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            mimeMessage.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
            mimeMessage.setRecipients(Message.RecipientType.TO, request.getCustomerEmail());
            mimeMessage.setReplyTo(new javax.mail.Address[]
                    {new javax.mail.internet.InternetAddress(springMailProperties.getSender().getFromEmail())});
            mimeMessage.setFrom(new InternetAddress(springMailProperties.getSender().getFromEmail(),
                    springMailProperties.getSender().getFromName()));
            mimeMessage.setSubject(springMailProperties.getSender().getSubject(), "utf-8");

            mimeMessage.setContent(multipart);
            mimeMessage.setSentDate(new Date());
            mimeMessage.saveChanges();
            javaMailSender.send(mimeMessage);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("found error email service: {}", e);
        }
    }
}
