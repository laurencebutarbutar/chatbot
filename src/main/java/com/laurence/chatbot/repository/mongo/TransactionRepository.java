package com.laurence.chatbot.repository.mongo;

import com.laurence.chatbot.entity.mongo.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    Transaction findFirstByInvoiceNumberAndStatusAndUsername(String invoiceNumber, String status, String username);
    List<Transaction> findAllByUsername(String username);
}
