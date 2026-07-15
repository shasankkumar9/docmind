package com.ssharma.docmind.repository;

import com.ssharma.docmind.dto.SearchResult;

import java.util.List;
import java.util.UUID;

public interface VectorRepository {

    void saveEmbedding(long chunkId, float[] embedding);

    List<SearchResult> findNearestChunks(UUID documentId, float[] embedding, int limit);

}