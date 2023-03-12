package com.laurence.chatbot.module.chat;

import com.laurence.chatbot.common.helper.MapperHelper;
import com.laurence.chatbot.config.properties.CacheProperties;
import com.laurence.chatbot.module.base.constant.CacheKeyConstant;
import com.laurence.chatbot.repository.cache.CacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheService {

    private final CacheProperties cacheProperties;
    private final CacheRepository cacheRepository;

    public <T> void set(String key, T value) {
        try {
            cacheRepository.set(key, MapperHelper.convert(value));
        } catch (Exception e) {
            log.error("#ERROR set() for key : {} and value : {}, caused by ", key, value, e);
        }
    }

    public <T> void set(String key, T value, Duration threshold) {
        try {
            cacheRepository.set(key, MapperHelper.convert(value), threshold);
        } catch (Exception e) {
            log.error("#ERROR set() for key : {}, value : {}, and threshold : {}, caused by ",
                    key,
                    value,
                    threshold,
                    e);
        }
    }

    public <T> T get(String key, Class<T> classType) {
        try {
            String value = cacheRepository.get(key);
            return StringUtils.isNotBlank(value) ? MapperHelper.convert(value, classType) : null;
        } catch (Exception e) {
            log.error("#ERROR get() for key : {} and classType : {}, caused by ", key, classType, e);
            return null;
        }
    }

    public <T> List<T> getAsList(String key, Class<T> classType) {
        try {
            String value = cacheRepository.get(key);
            return StringUtils.isNotBlank(value) ?
                    MapperHelper.convertAsList(value, classType) :
                    Collections.emptyList();
        } catch (Exception e) {
            log.error("#ERROR getAsList() for key : {} and classType : {}, caused by ",
                    key,
                    classType,
                    e);
            return Collections.emptyList();
        }
    }

    public String getValueAsString(String key) {
        try {
            return cacheRepository.get(key);
        } catch (Exception e) {
            log.error("#ERROR getValueAsString() for key : {}, caused by ", key, e);
            return null;
        }
    }

    public Set<String> getKeysByPattern(String pattern) {
        try {
            return cacheRepository.getKeysByPattern(pattern);
        } catch (Exception e) {
            log.error("#ERROR getKeysByPattern() for pattern : {}, caused by ", pattern, e);
            return Collections.emptySet();
        }
    }

    public boolean deleteByKey(String key) {
        try {
            return cacheRepository.deleteByKey(key);
        } catch (Exception e) {
            log.error("#ERROR deleteByKey() for key : {}, caused by ", key, e);
            return false;
        }
    }

    public boolean deleteByKeys(Set<String> keys) {
        try {
            return Optional.ofNullable(cacheRepository.deleteByKeys(keys))
                    .map(count -> count > 0)
                    .orElse(false);
        } catch (Exception e) {
            log.error("#ERROR deleteByKeys() for keys : {}, caused by ", keys, e);
            return false;
        }
    }

    public boolean deleteByPattern(String pattern) {
        if (StringUtils.startsWithIgnoreCase(pattern, CacheKeyConstant.BASE_KEY)) {
            Set<String> prohibitedPatterns = Optional.ofNullable(cacheProperties.getCommon())
                    .map(CacheProperties.Common::getProhibitedPatterns)
                    .orElse(Collections.emptySet());

            Set<String> keys = Optional.ofNullable(getKeysByPattern(pattern))
                    .orElse(Collections.emptySet())
                    .stream()
                    .filter(key -> isDeleted(prohibitedPatterns, key))
                    .collect(Collectors.toSet());

            return deleteByKeys(keys);
        } else {
            return false;
        }
    }

    private boolean isDeleted(Set<String> prohibitedPatterns, String key) {
        return prohibitedPatterns.stream()
                .noneMatch(pattern -> StringUtils.startsWithIgnoreCase(key, pattern));
    }

    public long increment(String key) {
        try {
            return cacheRepository.increment(key);
        } catch (Exception e) {
            log.error("#ERROR increment() for key : {}, caused by ", key, e);
            return 1;
        }
    }

    public boolean expire(String key, Duration threshold) {
        try {
            return cacheRepository.expire(key, threshold);
        } catch (Exception e) {
            log.error("#ERROR expire() for key : {} and threshold : {}, caused by ", key, threshold, e);
            return false;
        }
    }
}
