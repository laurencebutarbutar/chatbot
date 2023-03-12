package com.laurence.chatbot.entity.elasticsearch;

import com.laurence.chatbot.entity.elasticsearch.constant.DocumentConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = DocumentConstant.PROMO_CODE)
public class PromoCode {
    @Id
    private String code;
    private String createdBy;
    private String sponsoredBy;
    private Integer discount;
    private List<String> user;
    private String paymentMethod;
    private Integer minimumPayment;
    private Integer maxAmountDiscount;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    private Instant startDateTime;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    private Instant expiredDateTime;

    @Field(type = FieldType.Date, format = DateFormat.custom, pattern = "uuuu-MM-dd'T'HH:mm:ss.SSS")
    private Instant createdDate;

}
