package com.laurence.chatbot.repository.mongo;

import com.laurence.chatbot.entity.mongo.Email;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailRepository extends MongoRepository<Email, String> {
    Email findFirstByStatus(String status);
}
