package com.laurence.chatbot.repository.cache;

import java.time.Duration;
import java.util.Set;

public interface CacheRepository {

  void set(String key, String value);

  void set(String key, String value, Duration threshold);

  String get(String key);

  Set<String> getKeysByPattern(String pattern);

  Boolean deleteByKey(String key);

  Long deleteByKeys(Set<String> keys);

  Long increment(String key);

  Boolean expire(String key, Duration threshold);
}
