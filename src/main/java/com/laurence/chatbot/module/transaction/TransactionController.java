package com.laurence.chatbot.module.transaction;

import com.laurence.chatbot.common.logging.LogExecutionTime;
import com.laurence.chatbot.common.utils.AuthUser;
import com.laurence.chatbot.module.base.constant.ApiPath;
import com.laurence.chatbot.module.transaction.response.TransactionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = ApiPath.TRANSACTION,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    private final TransactionService transactionService;

    @LogExecutionTime
    @PostMapping(value = ApiPath.PAID)
    public TransactionResponse paidTransaction(@RequestAttribute @ApiIgnore AuthUser authUser,
                                               @RequestParam String invoiceNumber) {
        log.info("#paidTransaction() with username : {} and invoiceNumber : {}", authUser, invoiceNumber);
        return transactionService.updateTransaction(authUser, invoiceNumber);
    }
}
