package com.laurence.chatbot.module.transaction;

import com.laurence.chatbot.common.helper.MapperHelper;
import com.laurence.chatbot.common.utils.AuthUser;
import com.laurence.chatbot.config.properties.ModuleProperties;
import com.laurence.chatbot.entity.elasticsearch.PromoCode;
import com.laurence.chatbot.entity.mongo.Transaction;
import com.laurence.chatbot.enums.PaymentMethod;
import com.laurence.chatbot.enums.ResponseEnum;
import com.laurence.chatbot.enums.TransactionStatus;
import com.laurence.chatbot.exception.BusinessException;
import com.laurence.chatbot.module.transaction.request.TransactionRequest;
import com.laurence.chatbot.module.transaction.response.TransactionResponse;
import com.laurence.chatbot.repository.elasticsearch.PromoCodeRepository;
import com.laurence.chatbot.repository.mongo.TransactionRepository;
import com.laurence.chatbot.service.queue.publisher.TransactionNotificationPublisher;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ModuleProperties moduleProperties;
    private final TransactionNotificationPublisher transactionNotificationPublisher;
    private final PromoCodeRepository promoCodeRepository;

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

    public TransactionResponse updateTransaction(AuthUser authUser, String invoiceNumber, String code, PaymentMethod paymentMethod){
        Transaction transaction = transactionRepository.findFirstByInvoiceNumberAndStatusAndUsername(invoiceNumber, TransactionStatus.PENDING.name(), authUser.getUser().getUsername());
        if(ObjectUtils.isEmpty(transaction)){
            String message = String.format(ResponseEnum.INVOICE_NOT_FOUND.getMessage(), invoiceNumber, authUser.getUser().getUsername());
            throw new BusinessException(ResponseEnum.INVOICE_NOT_FOUND, message);
        }
        Integer totalAmount = transaction.getAmount();

        if(StringUtils.isNotEmpty(code)){
            PromoCode promoCodeResult = promoCodeRepository.findAllByCodeAndStartDateTimeBeforeAndExpiredDateTimeAfterAndUserIn(code, Instant.now(), Instant.now(), List.of(authUser.getUser().getUsername(), "ALL")).stream()
                    .filter(promoCode -> transaction.getAmount() > promoCode.getMinimumPayment())
                    .filter(promoCode -> paymentMethod.name().equals(promoCode.getPaymentMethod()))
                    .max(Comparator.comparing(PromoCode::getDiscount))
                    .orElseThrow(() -> {

                        String message = String.format(ResponseEnum.PROMO_CODE_NOT_FOUND.getMessage(), code, paymentMethod);
                        throw new BusinessException(ResponseEnum.PROMO_CODE_NOT_FOUND, message);
                    });

            Integer totalDiscount = promoCodeResult.getDiscount() * transaction.getAmount();
            if(totalDiscount > promoCodeResult.getMaxAmountDiscount()){
                totalDiscount = promoCodeResult.getMaxAmountDiscount();
            }

            totalAmount = transaction.getAmount() - totalDiscount;
            transaction.setDiscount(totalDiscount);
            transaction.setPromoCode(code);
        }
        transaction.setTotalAmount(totalAmount);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setPaymentDate(Instant.now());
        transaction.setStatus(TransactionStatus.SUCCESS.name());
        transactionRepository.save(transaction);
        TransactionResponse transactionResponse = MapperHelper.map(transaction, TransactionResponse.class);
        transactionNotificationPublisher.publish(transactionResponse);
        return transactionResponse;
    }

    public List<TransactionResponse> listTransaction(AuthUser authUser){
        List<Transaction> transactions = transactionRepository.findAllByUsernameOrderByCreatedDateDesc(authUser.getUser().getUsername());
        return MapperHelper.mapAsList(transactions, TransactionResponse.class);
    }

}
