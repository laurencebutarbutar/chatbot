package com.laurence.chatbot.repository.mongo;

import com.laurence.chatbot.entity.mongo.Promo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PromoRepository extends MongoRepository<Promo, String> {

}
