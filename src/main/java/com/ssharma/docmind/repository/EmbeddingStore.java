package com.ssharma.docmind.repository;

import com.ssharma.docmind.dto.SearchResult;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class EmbeddingStore {

    private final JdbcTemplate jdbcTemplate;

    public EmbeddingStore(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(long chunkId, float[] embedding) {

        String vector = toVector(embedding);

        jdbcTemplate.update("""
                        INSERT INTO chunk_embeddings(chunk_id, embedding)
                        VALUES (?, ?::vector)
                        """,
                chunkId,
                vector);

    }

    private String toVector(float[] embedding) {

        StringBuilder builder = new StringBuilder("[");

        for (int i = 0; i < embedding.length; i++) {

            if (i > 0) {
                builder.append(",");
            }

            builder.append(embedding[i]);

        }

        builder.append("]");

        return builder.toString();

    }

    public List<SearchResult> findNearest(UUID documentId,
                                          float[] embedding,
                                          int limit) {

        String vector = toVector(embedding);

        return jdbcTemplate.query(
                """
                        SELECT
                            ce.chunk_id,
                            ce.embedding <=> ?::vector AS distance
                        FROM chunk_embeddings ce
                        JOIN document_chunks dc
                            ON ce.chunk_id = dc.id
                        WHERE dc.document_id = ?
                        ORDER BY distance
                        LIMIT ?
                        """,
                (rs, rowNum) ->
                        new SearchResult(
                                rs.getLong("chunk_id"),
                                rs.getDouble("distance")
                        ),
                vector,
                documentId,
                limit
        );

    }

}