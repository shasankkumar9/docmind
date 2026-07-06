package com.ssharma.docmind.chat.controller;

import com.ssharma.docmind.chat.dto.ChatRequest;
import com.ssharma.docmind.chat.dto.ChatResponse;
import com.ssharma.docmind.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatResponse chat(
            @RequestBody ChatRequest request
    ) {

        String response =
                chatService.chat(request.message());

        return new ChatResponse(response);
    }
}
