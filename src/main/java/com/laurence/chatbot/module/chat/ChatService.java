package com.laurence.chatbot.module.chat;

import com.laurence.chatbot.module.chat.request.ChatMessageRequest;
import com.laurence.chatbot.module.chat.response.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    public ChatMessageResponse checkState(ChatMessageRequest chatMessageRequest){
        return ChatMessageResponse.builder()
                .message("OK")
                .build();
    }
}
