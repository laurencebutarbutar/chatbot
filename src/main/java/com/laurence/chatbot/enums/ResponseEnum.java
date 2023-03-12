package com.laurence.chatbot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseEnum {

    SUCCESS(HttpStatus.OK,
            "SUCCESS",
            "success",
            "success"),
    INTERNAL_ERROR(HttpStatus.SERVICE_UNAVAILABLE,
            "Sorry, there must be something wrong with the service.",
            "api_error",
            "internal_service_error"),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST,
            "User '%s' not found, please check again.",
            "failed_login",
            "user_not_found"),
    INVOICE_NOT_FOUND(HttpStatus.BAD_REQUEST,
            "Invoice number '%s' from User '%s' not found.",
            "failed_transaction",
            "invoice_not_found"),
    FAILED_PASSWORD(HttpStatus.BAD_REQUEST,
            "Password not match for user '%s', please try again",
            "failed_login",
            "password_not_match"),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST,
            "The request for %s is invalid, because %s.",
            "invalid_request_error",
            "invalid_request_body"),
    INVALID_REQUEST_DB(HttpStatus.BAD_REQUEST,
            "Invalid Request DB : ",
            "invalid_request_error",
            "invalid_request_database");

    private final HttpStatus status;
    private final String message;
    private final String type;
    private final String code;
}
