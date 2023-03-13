package com.laurence.chatbot.entity.mongo;

import com.laurence.chatbot.enums.PaymentMethod;
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
    private Integer amount;
    private Instant createdDate;
    private Instant paymentDate;
    private String invoiceNumber;
    private String username;
    private String status;
    private String promoCode;
    private Integer discount;
    private Integer totalAmount;
    private PaymentMethod paymentMethod;

}
