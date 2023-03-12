package com.laurence.chatbot.entity.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "promo")
public class Promo {
    @Id
    private String id;

    private String code;
    private Instant startDateTime;
    private Instant expiredDateTime;
    private String createdBy;
    private Instant createdDate;
    private String sponsoredBy;
    private Integer discount;
    private List<String> user;
    private String paymentMethod;
    private Integer minimumPayment;
    private Integer maxAmountDiscount;

}
