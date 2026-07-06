package com.ssharma.docmind.document.repository;

import com.ssharma.docmind.document.entity.Document;
import com.ssharma.docmind.document.entity.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository
        extends JpaRepository<Document, UUID> {

    List<Document> findAllByStatus(DocumentStatus status);

}