package com.ssharma.docmind.service;

import com.ssharma.docmind.config.RagProperties;
import com.ssharma.docmind.dto.SearchResult;
import com.ssharma.docmind.entity.DocumentChunk;
import com.ssharma.docmind.repository.DocumentChunkRepository;
import com.ssharma.docmind.repository.EmbeddingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RetrievalService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(RetrievalService.class);

    private final EmbeddingService embeddingService;
    private final EmbeddingStore embeddingStore;
    private final DocumentChunkRepository chunkRepository;
    private final RagProperties ragProperties;

    public RetrievalService(EmbeddingService embeddingService,
                            EmbeddingStore embeddingStore,
                            DocumentChunkRepository chunkRepository,
                            RagProperties ragProperties) {

        this.embeddingService = embeddingService;
        this.embeddingStore = embeddingStore;
        this.chunkRepository = chunkRepository;
        this.ragProperties = ragProperties;
    }

    public List<DocumentChunk> retrieve(UUID documentId, String question) {

        LOGGER.info("Searching document {}", documentId);
        LOGGER.info("Question: {}", question);

        float[] embedding = embeddingService.embed(question);

        List<SearchResult> results = embeddingStore.findNearest(
                documentId,
                embedding,
                ragProperties.getTopK()
        );

        LOGGER.info("Retrieved {} candidate chunks", results.size());

        if (!results.isEmpty()) {

            double min = results.stream()
                    .mapToDouble(SearchResult::distance)
                    .min()
                    .orElse(0);

            double max = results.stream()
                    .mapToDouble(SearchResult::distance)
                    .max()
                    .orElse(0);

            double avg = results.stream()
                    .mapToDouble(SearchResult::distance)
                    .average()
                    .orElse(0);

            LOGGER.info("Distance Statistics");
            LOGGER.info("Minimum : {}", min);
            LOGGER.info("Average : {}", avg);
            LOGGER.info("Maximum : {}", max);

        }

        results.forEach(result ->
                LOGGER.info(
                        "Chunk {} -> Distance {}",
                        result.chunkId(),
                        result.distance()
                ));

        List<SearchResult> filteredResults = results.stream()
                .filter(result ->
                        result.distance() <= ragProperties.getSimilarityThreshold())
                .toList();

        LOGGER.info(
                "After similarity threshold ({}): {} chunks",
                ragProperties.getSimilarityThreshold(),
                filteredResults.size()
        );

        if (filteredResults.isEmpty()) {

            LOGGER.warn("No chunks passed the similarity threshold.");

            return List.of();

        }

        List<Long> ids = filteredResults.stream()
                .map(SearchResult::chunkId)
                .toList();

        Map<Long, DocumentChunk> chunkMap =
                chunkRepository.findByDocumentIdAndIdIn(documentId, ids)
                        .stream()
                        .collect(Collectors.toMap(
                                DocumentChunk::getId,
                                Function.identity()
                        ));

        results.forEach(result -> {

            DocumentChunk chunk = chunkMap.get(result.chunkId());

            String preview = "";

            if (chunk != null) {

                preview = chunk.getContent();

                if (preview.length() > 100) {
                    preview = preview.substring(0, 100) + "...";
                }

            }

            LOGGER.info(
                    "Distance={} | Chunk={} | {}",
                    result.distance(),
                    result.chunkId(),
                    preview
            );

        });

        List<DocumentChunk> chunks = filteredResults.stream()
                .map(result -> chunkMap.get(result.chunkId()))
                .filter(Objects::nonNull)
                .toList();

        LOGGER.info("Returning {} chunks to PromptService", chunks.size());

        return chunks;
    }
}
