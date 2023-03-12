package com.laurence.chatbot.common.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperHelper {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();
    private static final ObjectMapper OBJECT_MAPPER = Jackson2ObjectMapperBuilder.json()
            .modules(new JavaTimeModule())
            .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .featuresToEnable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL,
                    MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
            .build();

    public static <S, T> T map(S source, Class<T> target) {
        if (ObjectUtils.isEmpty(source) || ObjectUtils.isEmpty(target)) {
            return null;
        } else {
            return MODEL_MAPPER.map(source, target);
        }
    }

    public static <S, T> List<T> mapAsList(Collection<S> sources, Class<T> target) {
        if (CollectionUtils.isEmpty(sources) || ObjectUtils.isEmpty(target)) {
            return Collections.emptyList();
        } else {
            return sources.stream()
                    .map(source -> map(source, target))
                    .collect(Collectors.toList());
        }
    }

    public static String convert(Object source) {
        try {
            if (Objects.isNull(source)) {
                return StringUtils.EMPTY;
            } else if (source instanceof byte[]) {
                return new String((byte[]) source, Charset.defaultCharset());
            } else if (source instanceof String) {
                return String.valueOf(source);
            } else {
                return OBJECT_MAPPER.writeValueAsString(source);
            }
        } catch (Exception e) {
            log.error("#ERROR convert() for source : {}, caused by ", source, e);
            return StringUtils.EMPTY;
        }
    }

    public static Map<String, Object> convertToMap(Object source) {
        try {
            return OBJECT_MAPPER.convertValue(source, new TypeReference<>() {});
        } catch (Exception e) {
            log.error("#ERROR convertToMap() for source : {}, caused by ", source, e);
            return null;
        }
    }

    public static <T> T convert(String source, Class<T> target) {
        try {
            return OBJECT_MAPPER.readValue(source, target);
        } catch (Exception e) {
            log.error("#ERROR convert() for source : {} and target : {}, caused by ", source, target, e);
            return null;
        }
    }

    public static <T> List<T> convertAsList(String source, Class<T> target) {
        try {
            return OBJECT_MAPPER.readValue(source,
                    OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, target));
        } catch (Exception e) {
            log.error("#ERROR convertAsList() for source : {} and target : {}, caused by ",
                    source,
                    target,
                    e);
            return Collections.emptyList();
        }
    }
}
