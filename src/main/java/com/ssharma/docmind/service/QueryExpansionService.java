package com.ssharma.docmind.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class QueryExpansionService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(QueryExpansionService.class);

    private final ChatClient chatClient;

    public QueryExpansionService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String expand(String question) {

        String prompt = """
                Rewrite the following search query for semantic vector retrieval.
                
                Rules:
                
                - Preserve the original meaning.
                - Expand abbreviations if applicable.
                - Include closely related technical keywords and synonyms.
                - Do NOT answer the question.
                - Do NOT invent information.
                - Return only the rewritten search query.
                
                User Query:
                
                %s
                """.formatted(question);

        String expandedQuery = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        LOGGER.info("========== Query Expansion ==========");
        LOGGER.info("Original : {}", question);
        LOGGER.info("Expanded : {}", expandedQuery);
        LOGGER.info("=====================================");

        return expandedQuery;
    }

}