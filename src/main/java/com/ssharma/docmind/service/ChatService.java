package com.ssharma.docmind.service;

import com.ssharma.docmind.dto.ChatResponse;
import com.ssharma.docmind.dto.RetrievedChunk;
import com.ssharma.docmind.dto.SourceDto;
import com.ssharma.docmind.exception.ChatException;
import com.ssharma.docmind.service.llm.LlmService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ChatService.class);

    private static final String NO_INFORMATION_FOUND =
            "I couldn't find any relevant information in the uploaded document.";

    private final RetrievalService retrievalService;
    private final PromptService promptService;
    private final LlmService llmService;

    public ChatService(RetrievalService retrievalService,
                       PromptService promptService,
                       LlmService llmService) {

        this.retrievalService = retrievalService;
        this.promptService = promptService;
        this.llmService = llmService;
    }

    /**
     * Answers a question using the supplied document.
     */
    public ChatResponse chat(UUID documentId,
                             String question) {

        long start = System.currentTimeMillis();

        LOGGER.info("Generating answer for document {}", documentId);

        try {

            List<RetrievedChunk> retrievedChunks =
                    retrievalService.retrieve(documentId, question);

            if (retrievedChunks.isEmpty()) {

                LOGGER.info("No relevant chunks found.");

                return new ChatResponse(
                        NO_INFORMATION_FOUND,
                        List.of()
                );

            }

            String prompt =
                    promptService.buildPrompt(
                            retrievedChunks,
                            question
                    );

            LOGGER.debug("Prompt:\n{}", prompt);

            String answer = generateAnswer(prompt);

            List<SourceDto> sources =
                    buildSources(retrievedChunks);

            LOGGER.info(
                    "Chat completed in {} ms",
                    System.currentTimeMillis() - start
            );

            return new ChatResponse(
                    answer,
                    sources
            );

        } catch (ChatException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new ChatException(
                    "Failed to generate AI response.",
                    ex
            );

        }

    }

    /**
     * Calls the LLM.
     */
    private String generateAnswer(String prompt) {

        return llmService.generate(prompt);

    }


    /**
     * Builds source citations for the response.
     */
    private List<SourceDto> buildSources(
            List<RetrievedChunk> chunks) {

        return chunks.stream()
                .map(chunk -> new SourceDto(

                        chunk.chunk().getChunkIndex(),

                        chunk.similarity(),

                        preview(chunk.chunk().getContent())

                ))
                .toList();

    }

    /**
     * Creates a short preview of a chunk.
     */
    private String preview(String text) {

        if (text == null) {
            return "";
        }

        text = text.replaceAll("\\s+", " ").trim();

        return text.length() <= 150
                ? text
                : text.substring(0, 150) + "...";

    }

}