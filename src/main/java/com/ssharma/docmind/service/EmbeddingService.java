package com.ssharma.docmind.service;

import com.ssharma.docmind.entity.DocumentChunk;
import com.ssharma.docmind.exception.EmbeddingException;
import com.ssharma.docmind.repository.PgVectorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(EmbeddingService.class);

    private final EmbeddingModel embeddingModel;
    private final PgVectorRepository pgVectorRepository;

    public EmbeddingService(EmbeddingModel embeddingModel,
                            PgVectorRepository pgVectorRepository) {

        this.embeddingModel = embeddingModel;
        this.pgVectorRepository = pgVectorRepository;

    }

    /**
     * Generates an embedding for the supplied text.
     * Used for semantic search queries.
     */
    public float[] embed(String text) {

        try {

            return embeddingModel.embed(text);

        } catch (Exception ex) {

            throw new EmbeddingException(
                    "Failed to generate embedding.",
                    ex
            );

        }

    }

    /**
     * Generates and stores embeddings for all document chunks.
     */
    public void generateEmbeddings(List<DocumentChunk> chunks) {

        long start = System.currentTimeMillis();

        LOGGER.info(
                "Generating embeddings for {} chunks.",
                chunks.size()
        );

        try {

            for (DocumentChunk chunk : chunks) {

                storeEmbedding(chunk);

            }

            LOGGER.info(
                    "Successfully generated {} embeddings in {} ms.",
                    chunks.size(),
                    System.currentTimeMillis() - start
            );

        } catch (EmbeddingException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new EmbeddingException(
                    "Failed to generate document embeddings.",
                    ex
            );

        }

    }

    /**
     * Generates and stores the embedding for a single chunk.
     */
    private void storeEmbedding(DocumentChunk chunk) {

        float[] embedding = embed(chunk.getContent());

        pgVectorRepository.saveEmbedding(
                chunk.getId(),
                embedding
        );

    }

}