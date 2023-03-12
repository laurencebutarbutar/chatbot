package com.laurence.chatbot.repository.mongo;

import com.laurence.chatbot.entity.mongo.Template;
import com.laurence.chatbot.enums.StateMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TemplateRepository extends MongoRepository<Template, String> {
    Template findFirstByState(StateMessage state);
}
