package com.ssharma.docmind.service;

import com.ssharma.docmind.entity.DocumentChunk;
import com.ssharma.docmind.repository.EmbeddingStore;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore embeddingStore;

    public EmbeddingService(EmbeddingModel embeddingModel,
                            EmbeddingStore embeddingStore) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    /**
     * Generate embedding for a single text.
     * Used for search queries and later HyDE.
     */
    public float[] embed(String text) {
        return embeddingModel.embed(text);
    }

    /**
     * Generate embeddings for document chunks and store them.
     */
    public void generateEmbeddings(List<DocumentChunk> chunks) {

        chunks.forEach(chunk -> {

            float[] embedding = embed(chunk.getContent());

            embeddingStore.save(
                    chunk.getId(),
                    embedding
            );

        });

    }

}