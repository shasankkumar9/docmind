package com.ssharma.docmind.controller;

import com.ssharma.docmind.dto.ChatRequest;
import com.ssharma.docmind.dto.ChatResponse;
import com.ssharma.docmind.service.ChatService;
import com.ssharma.docmind.swagger.ChatApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Chat",
        description = "AI-powered document question answering"
)
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @Operation(
            summary = "Ask a question",
            description = """
                    Searches the uploaded document using semantic search
                    and generates an answer using the configured local
                    language model.
                    """
    )
    @ChatApiResponses
    @PostMapping
    public ChatResponse chat(
            @Valid
            @RequestBody
            ChatRequest request) {

        return chatService.chat(
                request.getDocumentId(),
                request.getMessage()
        );

    }

}