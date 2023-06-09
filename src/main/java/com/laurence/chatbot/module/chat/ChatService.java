package com.laurence.chatbot.module.chat;

import com.laurence.chatbot.common.utils.AuthUser;
import com.laurence.chatbot.config.properties.CacheProperties;
import com.laurence.chatbot.config.properties.ModuleProperties;
import com.laurence.chatbot.entity.mongo.Template;
import com.laurence.chatbot.enums.StateMessage;
import com.laurence.chatbot.module.base.constant.CacheKeyConstant;
import com.laurence.chatbot.module.base.dto.StateSession;
import com.laurence.chatbot.module.chat.request.ChatMessageRequest;
import com.laurence.chatbot.module.chat.response.ChatMessageResponse;
import com.laurence.chatbot.module.transaction.TransactionService;
import com.laurence.chatbot.module.transaction.request.TransactionRequest;
import com.laurence.chatbot.module.transaction.response.TransactionResponse;
import com.laurence.chatbot.repository.mongo.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final CacheProperties cacheProperties;
    private final CacheService cacheService;
    private final ModuleProperties moduleProperties;
    private final TemplateRepository templateRepository;
    private final TransactionService transactionService;

    public ChatMessageResponse messageService(AuthUser authUser, ChatMessageRequest chatMessageRequest){
        String message = userStateMessage(authUser, chatMessageRequest);
        return ChatMessageResponse.builder()
                .message(message)
                .build();
    }

    private String buildReplyMessage(StateMessage stateMessage, String customerName, String invoiceNumber, List<String> transactionResponses){
        Template templateMessage = templateRepository.findFirstByState(stateMessage);
        String messsage = "";
        switch (stateMessage){
            case CHOOSE_MENU :
                messsage = String.format(templateMessage.getMessage(), customerName);
                break;
            case FINALIZE :
                messsage = String.format(templateMessage.getMessage(), invoiceNumber);
                break;
            case CHECK_STATUS:
                messsage = String.format(templateMessage.getMessage(), transactionResponses);
                break;
            default:
                messsage = templateMessage.getMessage();
        }

        return messsage;
    }

    private StateSession buildNextStateSession(StateSession stateSession, ChatMessageRequest chatMessageRequest, String cacheKey){
        StateMessage stateMessage;
        Map<String, String> params = stateSession.getParameter();
        StateSession nextStateSession;
            switch (stateSession.getState()) {
                case CHOOSE_MENU:
                    if(chatMessageRequest.getAnswer().equals("1")){
                        stateMessage = StateMessage.valueOf(stateSession.getState().getOrder() + 1);
                    }else{
                        stateMessage = StateMessage.valueOf(stateSession.getState().getOrder() + 10);
                    }
                    break;

                case INPUT_CUSTOMER_EMAIL:
                    stateMessage = StateMessage.valueOf(stateSession.getState().getOrder() + 1);
                    params = Map.of("customerEmail", chatMessageRequest.getAnswer());
                    break;

                case INPUT_AMOUNT:
                    stateMessage = StateMessage.valueOf(stateSession.getState().getOrder() + 1);
                    params.put("amount", chatMessageRequest.getAnswer());
                    break;

                case FINALIZE:
                case CHECK_STATUS:
                    stateMessage = null;
                    break;

                default:
                    stateMessage = StateMessage.CHOOSE_MENU;
            }

            if(ObjectUtils.isEmpty(stateMessage)){
                cacheService.deleteByKey(cacheKey);
                nextStateSession = null;
            }else{
                nextStateSession = StateSession.builder()
                        .state(stateMessage)
                        .parameter(params)
                        .build();
                cacheService.set(cacheKey, nextStateSession);
            }

            return nextStateSession;
    }

    private String userStateMessage(AuthUser authUser, ChatMessageRequest chatMessageRequest){
        String cacheKey = String.format(CacheKeyConstant.STATE_KEY, authUser.getUser().getUsername());
        StateSession stateSession = cacheService.get(cacheKey, StateSession.class);
        String message = "";
        if(ObjectUtils.isNotEmpty(stateSession)){
            if(isValidAnswer(stateSession, chatMessageRequest)){
                StateSession nextStateSession = buildNextStateSession(stateSession, chatMessageRequest, cacheKey);
                message = buildReplyMessage(nextStateSession.getState(), null, null, null);
                if(nextStateSession.getState().equals(StateMessage.FINALIZE) || nextStateSession.getState().equals(StateMessage.CHECK_STATUS)){
                    if(nextStateSession.getState().equals(StateMessage.FINALIZE)){
                        TransactionRequest transactionRequest = TransactionRequest.builder()
                                .amount(Integer.valueOf(nextStateSession.getParameter().get("amount")))
                                .customerEmail(nextStateSession.getParameter().get("customerEmail"))
                                .username(authUser.getUser().getUsername())
                                .build();
                        TransactionResponse transactionResponse = transactionService.generateTransaction(transactionRequest);
                        message = buildReplyMessage(nextStateSession.getState(), null, transactionResponse.getInvoiceNumber(), null);
                    }else{
                        List<TransactionResponse> transactionResponse = transactionService.listTransaction(authUser);
                        List<String> listMessageResponse = transactionResponse.stream()
                                .map(transactionResponseMessage -> transactionResponseMessage.getInvoiceNumber() + " - " + transactionResponseMessage.getTotalAmount() + " - " + transactionResponseMessage.getStatus())
                                .toList();
                        message = buildReplyMessage(nextStateSession.getState(), null, null, listMessageResponse);
                    }
                    buildNextStateSession(nextStateSession, chatMessageRequest, cacheKey);
                }
            }else{
                String messageLastState = buildReplyMessage(stateSession.getState(), authUser.getUser().getUsername(), null, null);
                String messageBadRequest = buildReplyMessage(StateMessage.BAD_REQUEST, null, null, null);
                message = messageBadRequest + " \n " + messageLastState;
            }
        }
        else{
            StateSession nextStateSession = StateSession.builder()
                    .state(StateMessage.CHOOSE_MENU)
                    .build();
            cacheService.set(cacheKey, nextStateSession);
            message = buildReplyMessage(nextStateSession.getState(), authUser.getUser().getUsername(), null, null);
        }

        return message;
    }

    private boolean isValidAnswer(StateSession stateSession, ChatMessageRequest chatMessageRequest){
        switch (stateSession.getState()){
            case CHOOSE_MENU :
                return chatMessageRequest.getAnswer().equals("1") ||  chatMessageRequest.getAnswer().equals("2");
            case INPUT_CUSTOMER_EMAIL:
                Pattern regexEmail = Pattern.compile(moduleProperties.getChat().getEmailPattern(), Pattern.CASE_INSENSITIVE);
                Matcher matcherEmail = regexEmail.matcher(chatMessageRequest.getAnswer());
                return matcherEmail.find();
            case INPUT_AMOUNT:
                Pattern regexAmount = Pattern.compile(moduleProperties.getChat().getAmountPattern(), Pattern.CASE_INSENSITIVE);
                Matcher matcherAmount = regexAmount.matcher(chatMessageRequest.getAnswer());
                return matcherAmount.find();
            default:
                return false;
        }
    }

}
