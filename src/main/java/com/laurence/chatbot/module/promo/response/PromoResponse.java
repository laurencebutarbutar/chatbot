package com.laurence.chatbot.module.promo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PromoResponse {
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
