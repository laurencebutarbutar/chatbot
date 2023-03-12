package com.laurence.chatbot.module.base.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiPath {

    // Module Cache
    public static final String CACHE = "/cache";
    public static final String CACHE_KEYS = "/keys";
    public static final String CACHE_VALUE = "/value";

    // Module User
    public static final String USER = "/user";
    public static final String LOGIN = "/login";

    // Module Chat
    public static final String CHAT = "/chat";
    public static final String MESSAGE = "/message";
}
