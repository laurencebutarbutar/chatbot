package com.laurence.chatbot.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transaction")
public class Transaction {
    @Id
    private String id;

    private String customerEmail;
    private String amount;
    private Instant createdDate;
    private String invoiceNumber;
    private String username;
    private String status;
}
