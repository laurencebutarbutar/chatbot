package com.laurence.chatbot.repository.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class RedisRepository implements CacheRepository {

  private final StringRedisTemplate stringRedisTemplate;

  @Override
  public void set(String key, String value) {
    stringRedisTemplate.opsForValue()
        .set(key, value);
  }

  @Override
  public void set(String key, String value, Duration threshold) {
    stringRedisTemplate.opsForValue()
        .set(key, value, threshold);
  }

  @Override
  public String get(String key) {
    return stringRedisTemplate.opsForValue()
        .get(key);
  }

  @Override
  public Set<String> getKeysByPattern(String pattern) {
    return stringRedisTemplate.keys(pattern);
  }

  @Override
  public Boolean deleteByKey(String key) {
    return stringRedisTemplate.delete(key);
  }

  @Override
  public Long deleteByKeys(Set<String> keys) {
    return stringRedisTemplate.delete(keys);
  }

  @Override
  public Long increment(String key) {
    return stringRedisTemplate.opsForValue()
        .increment(key);
  }

  @Override
  public Boolean expire(String key, Duration threshold) {
    return stringRedisTemplate.expire(key, threshold);
  }
}
