package com.ssharma.docmind.service;

import com.ssharma.docmind.config.RagProperties;
import com.ssharma.docmind.dto.RetrievedChunk;
import com.ssharma.docmind.dto.SearchResult;
import com.ssharma.docmind.entity.DocumentChunk;
import com.ssharma.docmind.exception.RetrievalException;
import com.ssharma.docmind.repository.DocumentChunkRepository;
import com.ssharma.docmind.repository.PgVectorRepository;
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
    private final PgVectorRepository pgVectorRepository;
    private final DocumentChunkRepository chunkRepository;
    private final RagProperties ragProperties;

    public RetrievalService(EmbeddingService embeddingService,
                            PgVectorRepository pgVectorRepository,
                            DocumentChunkRepository chunkRepository,
                            RagProperties ragProperties) {

        this.embeddingService = embeddingService;
        this.pgVectorRepository = pgVectorRepository;
        this.chunkRepository = chunkRepository;
        this.ragProperties = ragProperties;
    }

    /**
     * Retrieves the most relevant chunks for the supplied question.
     */
    public List<RetrievedChunk> retrieve(UUID documentId,
                                         String question) {

        long start = System.currentTimeMillis();

        LOGGER.info("Searching document {}", documentId);

        try {

            float[] embedding = embeddingService.embed(question);

            List<SearchResult> searchResults =
                    pgVectorRepository.findNearestChunks(
                            documentId,
                            embedding,
                            ragProperties.topK()
                    );

            logSearchStatistics(searchResults);

            List<SearchResult> filteredResults =
                    filterResults(searchResults);

            if (filteredResults.isEmpty()) {

                LOGGER.warn(
                        "No relevant chunks found for document {}",
                        documentId
                );

                return List.of();

            }

            List<RetrievedChunk> retrievedChunks =
                    loadChunks(documentId, filteredResults);

            LOGGER.info(
                    "Retrieved {} chunks in {} ms",
                    retrievedChunks.size(),
                    System.currentTimeMillis() - start
            );

            retrievedChunks.forEach(chunk ->
                    LOGGER.debug(
                            "Similarity={} | Chunk={} | {}",
                            String.format("%.4f", chunk.similarity()),
                            chunk.chunk().getChunkIndex(),
                            preview(chunk.chunk().getContent())
                    )
            );

            return retrievedChunks;

        } catch (RetrievalException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new RetrievalException(
                    "Failed to retrieve document chunks.",
                    ex
            );

        }

    }

    /**
     * Applies the configured similarity threshold.
     */
    private List<SearchResult> filterResults(List<SearchResult> results) {

        List<SearchResult> filtered = results.stream()
                .filter(result ->
                        result.distance()
                                <= ragProperties.similarityThreshold())
                .toList();

        LOGGER.info(
                "{} chunks passed similarity threshold ({})",
                filtered.size(),
                ragProperties.similarityThreshold()
        );

        return filtered;

    }

    /**
     * Loads the retrieved chunks from the database.
     */
    private List<RetrievedChunk> loadChunks(UUID documentId,
                                            List<SearchResult> results) {

        List<Long> ids = results.stream()
                .map(SearchResult::chunkId)
                .toList();

        Map<Long, DocumentChunk> chunkMap =
                chunkRepository.findByDocumentIdAndIdIn(
                                documentId,
                                ids
                        )
                        .stream()
                        .collect(Collectors.toMap(
                                DocumentChunk::getId,
                                Function.identity()
                        ));

        return results.stream()
                .map(result -> {

                    DocumentChunk chunk =
                            chunkMap.get(result.chunkId());

                    if (chunk == null) {
                        return null;
                    }

                    return new RetrievedChunk(
                            chunk,
                            result.distance()
                    );

                })
                .filter(Objects::nonNull)
                .toList();

    }

    /**
     * Logs vector search statistics.
     */
    private void logSearchStatistics(List<SearchResult> results) {

        LOGGER.info(
                "Retrieved {} candidate chunks",
                results.size()
        );

        if (results.isEmpty()) {
            return;
        }

        double min = results.stream()
                .mapToDouble(SearchResult::distance)
                .min()
                .orElse(0);

        double avg = results.stream()
                .mapToDouble(SearchResult::distance)
                .average()
                .orElse(0);

        double max = results.stream()
                .mapToDouble(SearchResult::distance)
                .max()
                .orElse(0);

        LOGGER.debug(
                "Distance statistics - min: {}, avg: {}, max: {}",
                min,
                avg,
                max
        );

    }

    /**
     * Creates a short preview for logging.
     */
    private String preview(String text) {

        if (text == null) {
            return "";
        }

        text = text.replaceAll("\\s+", " ").trim();

        return text.length() <= 100
                ? text
                : text.substring(0, 100) + "...";

    }

}