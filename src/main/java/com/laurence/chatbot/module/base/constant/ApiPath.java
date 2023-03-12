package com.laurence.chatbot.module.base.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiPath {

    // Module User
    public static final String USER = "/user";
    public static final String LOGIN = "/login";

    // Module Chat
    public static final String CHAT = "/chat";
    public static final String MESSAGE = "/message";

    // Module Transaction
    public static final String TRANSACTION = "/transaction";
    public static final String PAID = "/paid";

    // Module Promo
    public static final String PROMO = "/promo";
    public static final String ADD = "/add";
    public static final String LIST = "/list";
}
