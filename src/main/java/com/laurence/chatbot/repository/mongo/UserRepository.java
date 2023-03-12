package com.laurence.chatbot.repository.mongo;

import com.laurence.chatbot.entity.mongo.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findFirstByUsername(String username);
}
