package com.ssharma.docmind.service;

import com.ssharma.docmind.dto.ChatResponse;
import com.ssharma.docmind.dto.RetrievedChunk;
import com.ssharma.docmind.dto.SourceDto;
import com.ssharma.docmind.exception.ChatException;
import com.ssharma.docmind.service.llm.LlmService;
import com.ssharma.docmind.service.retrieval.RetrievalMode;
import com.ssharma.docmind.service.retrieval.RetrievalModeDetector;
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
            "I couldn't find enough information in the uploaded document to answer that confidently.";

    private final RetrievalService retrievalService;
    private final DocumentReconstructionService reconstructionService;
    private final PromptService promptService;
    private final LlmService llmService;
    private final RetrievalModeDetector retrievalModeDetector;

    public ChatService(RetrievalService retrievalService,
                       DocumentReconstructionService reconstructionService,
                       PromptService promptService,
                       LlmService llmService,
                       RetrievalModeDetector retrievalModeDetector) {

        this.retrievalService = retrievalService;
        this.reconstructionService = reconstructionService;
        this.promptService = promptService;
        this.llmService = llmService;
        this.retrievalModeDetector = retrievalModeDetector;

    }

    /**
     * Answers a question using the uploaded document.
     */
    public ChatResponse chat(UUID documentId,
                             String question) {

        long start = System.currentTimeMillis();

        LOGGER.info("Generating response for document {}", documentId);

        try {

            RetrievalMode retrievalMode =
                    retrievalModeDetector.detect(question);

            LOGGER.info("Retrieval Mode: {}", retrievalMode);

            LOGGER.info("Question: {}", question);

            return switch (retrievalMode) {

                case SEMANTIC -> answerWithSemanticSearch(
                        documentId,
                        question
                );

                case FULL_DOCUMENT -> answerWithFullDocument(
                        documentId,
                        question
                );

            };

        } catch (ChatException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new ChatException(
                    "Failed to generate AI response.",
                    ex
            );

        } finally {

            LOGGER.info(
                    "Completed in {} ms",
                    System.currentTimeMillis() - start
            );

        }

    }

    private ChatResponse answerWithSemanticSearch(
            UUID documentId,
            String question) {

        List<RetrievedChunk> chunks =
                retrievalService.retrieve(
                        documentId,
                        question
                );

        LOGGER.info("Retrieved {} chunks", chunks.size());

        chunks.forEach(chunk ->
                LOGGER.info(
                        "Chunk={} Distance={} Preview={}",
                        chunk.chunk().getChunkIndex(),
                        String.format("%.4f", chunk.similarity()),
                        preview(chunk.chunk().getContent())
                )
        );

        if (chunks.isEmpty()) {

            return new ChatResponse(
                    NO_INFORMATION_FOUND,
                    List.of()
            );

        }

        String context =
                chunks.stream()
                        .map(chunk -> chunk.chunk().getContent())
                        .reduce(
                                "",
                                (a, b) ->
                                        a + "\n\n---------------------\n\n" + b
                        );

        String prompt =
                promptService.buildPrompt(
                        context,
                        question
                );

        String answer =
                llmService.generate(prompt);

        return new ChatResponse(
                answer,
                buildSources(chunks)
        );

    }

    private ChatResponse answerWithFullDocument(
            UUID documentId,
            String question) {

        String context =
                retrievalService.retrieveFullDocument(
                        documentId
                );

        LOGGER.info(
                "Using full document context ({} characters)",
                context.length()
        );

        String prompt =
                promptService.buildPrompt(
                        context,
                        question
                );

        String answer =
                llmService.generate(prompt);

        return new ChatResponse(
                answer,
                List.of()
        );

    }

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
     * Creates a short preview.
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