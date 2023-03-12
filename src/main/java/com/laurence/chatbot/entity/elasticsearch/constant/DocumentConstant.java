package com.laurence.chatbot.entity.elasticsearch.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentConstant {
    public static final String BASE_NAME = "chatbot.";
    public static final String PROMO_CODE = BASE_NAME + "promo-code";
}
