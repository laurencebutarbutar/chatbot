package com.laurence.chatbot.module.transaction.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.laurence.chatbot.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionResponse {
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
