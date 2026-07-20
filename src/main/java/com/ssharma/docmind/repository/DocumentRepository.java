package com.ssharma.docmind.repository;

import com.ssharma.docmind.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {

    List<Document> findAllByOrderByUploadedAtDesc();

    Optional<Document> findByChecksum(String checksum);
}