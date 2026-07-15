# DocMind Architecture

## High-Level Flow

```
                User

                  │

                  ▼

        REST Controllers

                  │

      ┌───────────┴────────────┐

      ▼                        ▼

DocumentService          ChatService

      │                        │

      ▼                        ▼

 ParserService         RetrievalService

      │                        │

 Strategy Pattern      PgVectorRepository

      │                        │

TXT PDF DOCX XLSX      PostgreSQL + pgvector

      │

      ▼

 ChunkService

      ▼

EmbeddingService

      ▼

PgVectorRepository

      ▼

OllamaLlmService

      ▼

Ollama
```

---

## Upload Pipeline

1. Upload document.
2. Store metadata.
3. Parse text.
4. Create sentence-aware chunks.
5. Generate embeddings.
6. Store vectors in pgvector.

---

## Chat Pipeline

1. Receive user question.
2. Generate query embedding.
3. Vector similarity search.
4. Filter by similarity threshold.
5. Build prompt.
6. Generate answer using Ollama.
7. Return answer with citations.