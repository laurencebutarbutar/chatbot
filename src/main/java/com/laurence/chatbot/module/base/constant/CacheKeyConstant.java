package com.laurence.chatbot.module.base.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheKeyConstant {

    public static final String BASE_KEY = "chatbot::";
    public static final String STATE_KEY = BASE_KEY + "state.%s"; // username
}
