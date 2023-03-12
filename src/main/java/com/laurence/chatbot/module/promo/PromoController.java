package com.laurence.chatbot.module.promo;

import com.laurence.chatbot.common.logging.LogExecutionTime;
import com.laurence.chatbot.common.utils.AuthUser;
import com.laurence.chatbot.module.base.constant.ApiPath;
import com.laurence.chatbot.module.promo.response.AddPromoResponse;
import com.laurence.chatbot.module.promo.response.PromoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = ApiPath.PROMO,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class PromoController {

    private final PromoService promoService;

    @LogExecutionTime
    @PostMapping(value = ApiPath.ADD)
    public AddPromoResponse addPromoToElastic() {
        log.info("#addPromoToElastic()");
        return promoService.insertElastic();
    }

    @LogExecutionTime
    @GetMapping(value = ApiPath.LIST)
    public List<PromoResponse> listPromoUser(@RequestAttribute @ApiIgnore AuthUser authUser,
                                             @RequestParam(required = false) String code) {
        log.info("#listPromoUser() with username : {} and code : {}", authUser, code);
        return promoService.getAllPromoForUser(authUser, code);
    }
}
