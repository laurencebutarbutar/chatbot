package com.laurence.chatbot.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RetryableException extends RuntimeException{

    private final String message;

    public RetryableException(String message) {
        super(message);
        this.message = message;
    }
}
