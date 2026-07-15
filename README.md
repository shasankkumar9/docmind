# DocMind AI

DocMind AI is an AI-powered document question answering system built with Spring Boot, Spring AI, PostgreSQL (pgvector),
and Ollama. It enables users to upload documents, perform semantic search using vector embeddings, and ask natural
language questions with source citations.

The project is designed to run completely offline using local language models, making it suitable for privacy-sensitive
document processing without relying on external AI APIs.

---

## Features

- Multi-format document upload
    - TXT
    - PDF
    - DOCX
    - XLSX

- Sentence-aware document chunking

- Semantic search using pgvector

- Local embedding generation with Ollama

- AI-powered question answering

- Source citations with similarity scores

- Dockerized PostgreSQL + pgvector

- Automatic database initialization

- Clean layered architecture

- Custom exception handling

---

## Technology Stack

### Backend

- Java 25
- Spring Boot
- Spring AI
- Spring Data JPA
- PostgreSQL
- pgvector
- Ollama

### Document Processing

- Apache PDFBox
- Apache POI

### Database

- PostgreSQL 16
- pgvector

### Build

- Maven
- Docker Compose

---

## Architecture

![Architecture](docs/images/architecture.png)

A detailed explanation of the architecture can be found in:

docs/architecture.md

---

## Project Structure

```text
src/main/java/com/ssharma/docmind

├── config
├── controller
├── dto
├── entity
├── exception
├── parser
├── repository
├── service
│   └── llm
```

---

## RAG Pipeline

```
Document Upload
        │
        ▼
Document Parsing
        │
        ▼
Sentence-aware Chunking
        │
        ▼
Embedding Generation
        │
        ▼
pgvector Storage
        │
        ▼
Semantic Retrieval
        │
        ▼
Prompt Construction
        │
        ▼
Ollama
        │
        ▼
Answer with Source Citations
```

---

## Supported Document Formats

| Format | Status |
|--------|--------|
| TXT    | ✅      |
| PDF    | ✅      |
| DOCX   | ✅      |
| XLSX   | ✅      |

---

## Getting Started

### Prerequisites

- Java 25
- Docker Desktop
- Ollama

### Clone

```bash
git clone https://github.com/<your-username>/docmind.git

cd docmind
```

### Start PostgreSQL

```bash
docker compose up -d
```

### Download Ollama Models

```bash
ollama pull qwen3:1.7b

ollama pull nomic-embed-text
```

or simply run

```bash
docker/setup.bat
```

### Run the Application

```bash
mvn spring-boot:run
```

---

## API

### Upload Document

```
POST /api/documents
```

### Ask Questions

```
POST /api/chat
```

Detailed API documentation is available in:

docs/api.md

---

## Design Decisions

### Why pgvector?

- Runs locally
- Open source
- PostgreSQL integration
- No additional infrastructure

### Why Ollama?

- Offline inference
- No API costs
- Multiple local model support

### Why Sentence-aware Chunking?

Fixed-size chunking produced:

- Broken words
- Broken sentences
- Poor source previews

Sentence-aware chunking significantly improved retrieval quality and citation readability.

### Why Not HyDE?

HyDE was evaluated but removed after testing because it produced hallucinated hypothetical documents and reduced
retrieval accuracy for structured documents.

### Why Not LLM Query Expansion?

LLM-based query expansion increased latency (30–50 seconds per request) without providing meaningful improvements over
semantic vector search.

---

## Future Enhancements

- React frontend
- Streaming responses
- OCR integration for scanned documents
- Multi-document chat
- Hybrid search (Vector + Keyword)
- Conversation history
- Batch embedding insertion
- Reranking

---

## License

This project is licensed under the MIT License.

---

## Author

**Shasank Kumar Sharma**

Java Full Stack Engineer

GitHub: https://github.com/<your-username>

LinkedIn: https://linkedin.com/in/<your-profile>