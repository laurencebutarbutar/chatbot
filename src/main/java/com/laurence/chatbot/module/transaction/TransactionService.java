package com.laurence.chatbot.module.transaction;

import com.laurence.chatbot.common.helper.MapperHelper;
import com.laurence.chatbot.common.utils.AuthUser;
import com.laurence.chatbot.config.properties.ModuleProperties;
import com.laurence.chatbot.entity.mongo.Transaction;
import com.laurence.chatbot.enums.ResponseEnum;
import com.laurence.chatbot.enums.TransactionStatus;
import com.laurence.chatbot.exception.BusinessException;
import com.laurence.chatbot.module.transaction.request.TransactionRequest;
import com.laurence.chatbot.module.transaction.response.TransactionResponse;
import com.laurence.chatbot.repository.mongo.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ModuleProperties moduleProperties;

    public TransactionResponse generateTransaction(TransactionRequest transactionRequest){
        String dateFormat = Optional.ofNullable(moduleProperties.getTransaction())
                .map(ModuleProperties.Transaction::getDatePattern)
                .map(pattern -> new SimpleDateFormat(pattern).format(new Date()))
                .orElse(UUID.randomUUID().toString());
        String invoiceNumber = moduleProperties.getTransaction().getInvoicePrefix() + dateFormat;
        Transaction transaction = MapperHelper.map(transactionRequest, Transaction.class);
        transaction.setCreatedDate(Instant.now());
        transaction.setInvoiceNumber(invoiceNumber);
        transaction.setStatus(TransactionStatus.PENDING.name());
        transactionRepository.save(transaction);
        return MapperHelper.map(transaction, TransactionResponse.class);
    }

    public TransactionResponse updateTransaction(AuthUser authUser, String invoiceNumber){
        Transaction transaction = transactionRepository.findFirstByInvoiceNumberAndStatusAndUsername(invoiceNumber, TransactionStatus.PENDING.name(), authUser.getUser().getUsername());
        if(ObjectUtils.isEmpty(transaction)){
            String message = String.format(ResponseEnum.INVOICE_NOT_FOUND.getMessage(), invoiceNumber, authUser.getUser().getUsername());
            throw new BusinessException(ResponseEnum.INVOICE_NOT_FOUND, message);
        }
        transaction.setStatus(TransactionStatus.SUCCESS.name());
        transactionRepository.save(transaction);
        return MapperHelper.map(transaction, TransactionResponse.class);
    }

}
