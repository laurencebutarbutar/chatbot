package com.laurence.chatbot.module.promo;

import com.laurence.chatbot.common.helper.MapperHelper;
import com.laurence.chatbot.common.utils.AuthUser;
import com.laurence.chatbot.entity.elasticsearch.PromoCode;
import com.laurence.chatbot.entity.mongo.Promo;
import com.laurence.chatbot.module.promo.response.AddPromoResponse;
import com.laurence.chatbot.module.promo.response.PromoResponse;
import com.laurence.chatbot.repository.elasticsearch.PromoCodeRepository;
import com.laurence.chatbot.repository.mongo.PromoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromoService {

    private final PromoRepository promoRepository;
    private final PromoCodeRepository promoCodeRepository;

    public AddPromoResponse insertElastic(){
        List<Promo> listPromo = promoRepository.findAll();
        List<PromoCode> listPromoCode = listPromo.stream()
                .map(promo -> MapperHelper.map(promo, PromoCode.class))
                .toList();
        promoCodeRepository.saveAll(listPromoCode);
        return AddPromoResponse.builder()
                .totalDataUpload(listPromo.size())
                .build();
    }

    public List<PromoResponse> getAllPromoForUser(AuthUser authUser, String code){
        return Optional.ofNullable(code)
                .map(promoCode -> promoCodeRepository.findAllByCodeStartsWithAndStartDateTimeBeforeAndExpiredDateTimeAfterAndUserIn(promoCode, Instant.now(), Instant.now(), List.of(authUser.getUser().getUsername(), "ALL")))
                .orElse(promoCodeRepository.findAllByStartDateTimeBeforeAndExpiredDateTimeAfterAndUserIn(Instant.now(), Instant.now(), List.of(authUser.getUser().getUsername(), "ALL")))
                .stream()
                .map(promoCode -> MapperHelper.map(promoCode, PromoResponse.class))
                .collect(Collectors.toList());
    }


}
