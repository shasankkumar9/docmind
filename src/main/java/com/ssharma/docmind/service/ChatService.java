package com.ssharma.docmind.service;

import com.ssharma.docmind.entity.DocumentChunk;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final RetrievalService retrievalService;
    private final PromptService promptService;

    public ChatService(ChatClient chatClient,
                       RetrievalService retrievalService,
                       PromptService promptService) {

        this.chatClient = chatClient;
        this.retrievalService = retrievalService;
        this.promptService = promptService;
    }

    public String chat(UUID documentId,
                       String question) {

        List<DocumentChunk> chunks =
                retrievalService.retrieve(
                        documentId,
                        question
                );

        String prompt =
                promptService.buildPrompt(question, chunks);

        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();

    }

}