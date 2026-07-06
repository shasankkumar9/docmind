package com.ssharma.docmind.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatClient chatClient;

    public String chat(String message) {

        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}