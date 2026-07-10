package com.ssharma.docmind.service;

import com.ssharma.docmind.dto.SearchResult;
import com.ssharma.docmind.entity.DocumentChunk;
import com.ssharma.docmind.repository.DocumentChunkRepository;
import com.ssharma.docmind.repository.EmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RetrievalService {

    private final EmbeddingService embeddingService;
    private final EmbeddingStore embeddingStore;
    private final DocumentChunkRepository chunkRepository;
    @Value("${app.rag.top-k}")
    private int topK;

    public RetrievalService(EmbeddingService embeddingService,
                            EmbeddingStore embeddingStore,
                            DocumentChunkRepository chunkRepository) {

        this.embeddingService = embeddingService;
        this.embeddingStore = embeddingStore;
        this.chunkRepository = chunkRepository;
    }

    public List<DocumentChunk> retrieve(UUID documentId, String question) {

        float[] embedding = embeddingService.embed(question);

        List<SearchResult> results =
                embeddingStore.findNearest(
                        documentId,
                        embedding,
                        topK
                );

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
                .map(result -> chunkMap.get(result.chunkId()))
                .filter(Objects::nonNull)
                .toList();
    }

}