package com.laurence.chatbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum StateMessage {

    BAD_REQUEST(0),
    CHOOSE_MENU(1),
    INPUT_CUSTOMER_EMAIL(2),
    INPUT_AMOUNT(3),
    FINALIZE(4),
    CHECK_STATUS(11);

    private final Integer order;

    public static StateMessage valueOf(int order) {
        return Stream.of(values())
                .filter(stateMessage -> stateMessage.order == order)
                .findFirst()
                .orElse(BAD_REQUEST);
    }
}
