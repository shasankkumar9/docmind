package com.ssharma.docmind.controller;

import com.ssharma.docmind.dto.ChatRequest;
import com.ssharma.docmind.dto.ChatResponse;
import com.ssharma.docmind.service.ChatService;
import com.ssharma.docmind.service.RetrievalService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final RetrievalService retrievalService;

    public ChatController(ChatService chatService, RetrievalService retrievalService) {
        this.chatService = chatService;
        this.retrievalService = retrievalService;
    }

    @PostMapping
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {

        String response = chatService.chat(
                request.getDocumentId(),
                request.getMessage()
        );

        return new ChatResponse(response);

    }

}