package com.ssharma.docmind.service;

import com.ssharma.docmind.entity.DocumentChunk;
import com.ssharma.docmind.repository.DocumentChunkRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentReconstructionService {

    private final DocumentChunkRepository repository;

    public DocumentReconstructionService(
            DocumentChunkRepository repository) {

        this.repository = repository;

    }

    public String reconstruct(UUID documentId) {

        List<DocumentChunk> chunks =
                repository.findByDocumentIdOrderByChunkIndex(documentId);

        return chunks.stream()
                .map(DocumentChunk::getContent)
                .collect(Collectors.joining("\n\n"));

    }

}