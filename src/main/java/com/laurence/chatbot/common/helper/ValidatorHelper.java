package com.laurence.chatbot.common.helper;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorHelper {

    public static boolean isValid(Object value) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Errors errors = new BeanPropertyBindingResult(value, value.getClass().getName());
        new SpringValidatorAdapter(validator).validate(value, errors);
        if (errors.hasErrors()) {
            Map<String, List<String>> errorMap = convertToMap(errors.getFieldErrors());
            log.error("#ERROR requestIsValid() for object : {} and errorMap : {}", value, errorMap);
            return false;
        } else {
            return true;
        }
    }

    private static Map<String, List<String>> convertToMap(List<FieldError> fieldErrors) {
        return fieldErrors.stream()
                .collect(Collectors.groupingBy(FieldError::getField))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList())));
    }
}
