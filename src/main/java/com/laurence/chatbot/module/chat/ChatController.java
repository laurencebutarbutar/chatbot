package com.laurence.chatbot.module.chat;

import com.laurence.chatbot.common.logging.LogExecutionTime;
import com.laurence.chatbot.common.utils.AuthUser;
import com.laurence.chatbot.module.base.constant.ApiPath;
import com.laurence.chatbot.module.chat.request.ChatMessageRequest;
import com.laurence.chatbot.module.chat.response.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = ApiPath.CHAT,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatController {

    private final ChatService chatService;

    @LogExecutionTime
    @PostMapping(value = ApiPath.MESSAGE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ChatMessageResponse chat(@RequestBody @Valid ChatMessageRequest chatMessageRequest, @RequestAttribute @ApiIgnore AuthUser authUser) {
        log.info("#chat() with username : {} and message : {}", authUser, chatMessageRequest);
        return chatService.checkState(chatMessageRequest);
    }
}
