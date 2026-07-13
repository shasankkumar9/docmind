package com.ssharma.docmind.controller;

import com.ssharma.docmind.dto.ChatRequest;
import com.ssharma.docmind.dto.ChatResponse;
import com.ssharma.docmind.service.ChatService;
import com.ssharma.docmind.service.QueryExpansionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    private final QueryExpansionService queryExpansionService;

    public ChatController(ChatService chatService, QueryExpansionService queryExpansionService) {
        this.chatService = chatService;
        this.queryExpansionService = queryExpansionService;
    }

    @PostMapping
    public ChatResponse chat(@Valid @RequestBody ChatRequest request) {

        String response = chatService.chat(
                request.getDocumentId(),
                request.getMessage()
        );

        return new ChatResponse(response);

    }

    @GetMapping("/expand")
    public String expand(@RequestParam String question) {

        return queryExpansionService.expand(question);

    }

}