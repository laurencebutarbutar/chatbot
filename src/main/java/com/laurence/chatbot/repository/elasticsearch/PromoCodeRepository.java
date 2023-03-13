package com.laurence.chatbot.repository.elasticsearch;

import com.laurence.chatbot.entity.elasticsearch.PromoCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PromoCodeRepository extends ElasticsearchRepository<PromoCode, Long> {

  PromoCode findFirstByCode(String code);

  List<PromoCode> findAllByCodeAndStartDateTimeBeforeAndExpiredDateTimeAfterAndUserIn(String code, Instant nowDate1, Instant nowDate2, List<String> users);

  List<PromoCode> findAllByCodeStartsWithAndStartDateTimeBeforeAndExpiredDateTimeAfterAndUserIn(String code, Instant nowDate1, Instant nowDate2, List<String> users);

  List<PromoCode> findAllByStartDateTimeBeforeAndExpiredDateTimeAfterAndUserIn(Instant nowDate1, Instant nowDate2, List<String> users);

  List<PromoCode> findAllByCodeStartsWith(String code, Pageable pageable);

  List<PromoCode> findAllByCodeContainsIgnoreCase(String code, Pageable pageable);
}
