package com.ssharma.docmind.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class PgVectorInitializer {

    private final JdbcTemplate jdbcTemplate;

    public PgVectorInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {

        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS chunk_embeddings
                (
                    chunk_id BIGINT PRIMARY KEY
                        REFERENCES document_chunks(id)
                        ON DELETE CASCADE,

                    embedding VECTOR(768)
                );
                """);

        jdbcTemplate.execute("""
                CREATE INDEX IF NOT EXISTS idx_chunk_embeddings
                ON chunk_embeddings
                USING hnsw (embedding vector_cosine_ops);
                """);

        Integer exists = jdbcTemplate.queryForObject("""
        SELECT COUNT(*)
        FROM pg_extension
        WHERE extname = 'vector'
        """, Integer.class);

        if (exists == null || exists == 0) {
            throw new IllegalStateException("""
        PostgreSQL extension 'vector' is not installed.

        Start the database using:
        docker compose up -d
        """);
        }
    }

}
