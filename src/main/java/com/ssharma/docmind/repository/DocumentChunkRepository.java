package com.ssharma.docmind.repository;

import com.ssharma.docmind.entity.Document;
import com.ssharma.docmind.entity.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentChunkRepository extends JpaRepository<DocumentChunk, Long> {

    List<DocumentChunk> findByDocument(Document document);

    List<DocumentChunk> findByIdIn(List<Long> ids);

    List<DocumentChunk> findByDocumentId(UUID documentId);

    List<DocumentChunk> findByDocumentIdAndIdIn(
            UUID documentId,
            List<Long> ids
    );

    List<DocumentChunk> findByDocumentIdOrderByChunkIndex(UUID documentId);

}