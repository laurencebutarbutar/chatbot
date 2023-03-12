package com.laurence.chatbot.service.queue;

import com.laurence.chatbot.common.helper.MapperHelper;
import com.laurence.chatbot.common.helper.ValidatorHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseListener<T> {

    protected void consume(String topic, String payload) {
        try {
            log.info("#consume() with topic : {} and payload : {}", topic, payload);

            T request = MapperHelper.convert(payload, getPayloadType());
            processIfValid(topic, request);
        } catch (Exception e) {
            log.error("#ERROR consume() for topic : {} and payload : {}, caused by", topic, payload, e);
        }
    }

    private void processIfValid(String topic, T request) {
        if (isValid(request)) {
            process(request);
        } else {
            log.error("ERROR processIfValid() for topic : {} and request : {}", topic, request);
        }
    }

    private boolean isValid(T request) {
        return ObjectUtils.isNotEmpty(request) && ValidatorHelper.isValid(request);
    }

    protected abstract Class<T> getPayloadType();

    protected abstract void process(T request);
}
