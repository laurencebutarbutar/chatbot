package com.laurence.chatbot.exception.handler;

import com.laurence.chatbot.enums.ResponseEnum;
import com.laurence.chatbot.exception.BusinessException;
import com.laurence.chatbot.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> businessException(BusinessException e) {
        log.info("BusinessException : {}", e.getMessage());
        return ResponseEntity.status(e.getError().getStatus())
                .body(ErrorResponse.builder()
                        .error(ErrorResponse.Error.builder()
                                .code(String.valueOf(e.getError().getCode()))
                                .message(Optional.ofNullable(e.getMessage())
                                        .orElse(e.getError().getMessage()))
                                .type(e.getError().getType())
                                .build())
                        .build());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    public ErrorResponse nullPointerException(NullPointerException ex) {
        log.error("NullPointerException : {}", ex);
        ResponseEnum responseEnum = ResponseEnum.INTERNAL_ERROR;
        return ErrorResponse.builder()
                .error(ErrorResponse.Error.builder()
                        .code(String.valueOf(responseEnum.getCode()))
                        .message(responseEnum.getMessage())
                        .type(responseEnum.getType())
                        .build())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ErrorResponse bindException(BindException e) {
        log.warn(e.getMessage());
        ResponseEnum responseEnum = ResponseEnum.INVALID_REQUEST_BODY;
        return ErrorResponse.builder()
                .error(ErrorResponse.Error.builder()
                        .code(String.valueOf(responseEnum.getCode()))
                        .message(constructMessage(responseEnum, e.getBindingResult()))
                        .type(responseEnum.getType())
                        .build())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        ResponseEnum responseEnum = ResponseEnum.INVALID_REQUEST_BODY;
        return ErrorResponse.builder()
                .error(ErrorResponse.Error.builder()
                        .code(String.valueOf(responseEnum.getCode()))
                        .message(constructMessage(responseEnum, e.getBindingResult()))
                        .type(responseEnum.getType())
                        .build())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn(e.getMessage());
        ResponseEnum responseEnum = ResponseEnum.INVALID_REQUEST_BODY;
        return ErrorResponse.builder()
                .error(ErrorResponse.Error.builder()
                        .code(String.valueOf(responseEnum.getCode()))
                        .message(Optional.ofNullable(e.getCause())
                                .map(Throwable::getMessage)
                                .orElse(e.getMessage()))
                        .type(responseEnum.getType())
                        .build())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ErrorResponse httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.warn(e.getMessage());
        ResponseEnum responseEnum = ResponseEnum.INVALID_REQUEST_BODY;
        return ErrorResponse.builder()
                .error(ErrorResponse.Error.builder()
                        .code(String.valueOf(responseEnum.getCode()))
                        .message(Optional.ofNullable(e.getCause())
                                .map(Throwable::getMessage)
                                .orElse(e.getMessage()))
                        .type(responseEnum.getType())
                        .build())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse constraintViolationException(ConstraintViolationException e) {
        log.warn(e.getMessage());
        ResponseEnum responseEnum = ResponseEnum.INVALID_REQUEST_DB;
        return ErrorResponse.builder()
                .error(ErrorResponse.Error.builder()
                        .code(String.valueOf(responseEnum.getCode()))
                        .message(Optional.ofNullable(e.getCause())
                                .map(Throwable::getMessage)
                                .orElse(e.getMessage()))
                        .type(responseEnum.getType())
                        .build())
                .build();
    }

    private String constructMessage(ResponseEnum responseEnum, BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(FieldError::getField))
                .entrySet()
                .stream()
                .findFirst()
                .flatMap(entry -> entry.getValue()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .filter(StringUtils::isNotBlank)
                        .min(String::compareToIgnoreCase)
                        .map(value -> String.format(responseEnum.getMessage(), entry.getKey(), value)))
                .orElse(null);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse runtimeException(Exception ex) {
        log.error("Unknown Error", ex);
        ResponseEnum responseEnum = ResponseEnum.INTERNAL_ERROR;
        return ErrorResponse.builder()
                .error(ErrorResponse.Error.builder()
                        .code(String.valueOf(responseEnum.getCode()))
                        .message(responseEnum.getMessage())
                        .type(responseEnum.getType())
                        .build())
                .build();
    }

}
