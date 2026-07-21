package com.ssharma.docmind.service;

import com.ssharma.docmind.dto.DocumentSummaryDto;
import com.ssharma.docmind.dto.UploadResult;
import com.ssharma.docmind.entity.Document;
import com.ssharma.docmind.entity.DocumentChunk;
import com.ssharma.docmind.exception.ValidationException;
import com.ssharma.docmind.parser.DocumentParser;
import com.ssharma.docmind.parser.ParserService;
import com.ssharma.docmind.repository.DocumentChunkRepository;
import com.ssharma.docmind.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DocumentService.class);

    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository documentChunkRepository;
    private final ParserService parserService;
    private final ChunkService chunkService;
    private final EmbeddingService embeddingService;
    private final ChecksumService checksumService;

    public DocumentService(DocumentRepository documentRepository,
                           DocumentChunkRepository documentChunkRepository,
                           ParserService parserService,
                           ChunkService chunkService,
                           EmbeddingService embeddingService,
                           ChecksumService checksumService) {

        this.documentRepository = documentRepository;
        this.documentChunkRepository = documentChunkRepository;
        this.parserService = parserService;
        this.chunkService = chunkService;
        this.embeddingService = embeddingService;
        this.checksumService = checksumService;
    }

    public UploadResult upload(MultipartFile file) throws IOException {

        long start = System.currentTimeMillis();

        if (file.isEmpty()) {
            throw new ValidationException("Uploaded file is empty.", null);
        }

        LOGGER.info("Uploading document: {}", file.getOriginalFilename());

        String checksum = checksumService.calculate(file);

        Document existing = documentRepository
                .findByChecksum(checksum)
                .orElse(null);

        if (existing != null) {

            LOGGER.info(
                    "Duplicate document detected. Returning existing document id={}",
                    existing.getId()
            );

            return new UploadResult(
                    existing,
                    true
            );

        }

        Document document = new Document();

        document.setOriginalFileName(file.getOriginalFilename());
        document.setFileType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setUploadedAt(LocalDateTime.now(Clock.systemDefaultZone()));
        document.setChecksum(checksum);

        Document savedDocument =
                documentRepository.save(document);

        LOGGER.info(
                "Document saved with id={}",
                savedDocument.getId()
        );

        DocumentParser parser =
                parserService.getParser(file.getContentType());

        String extractedText =
                parser.extractText(file);

        LOGGER.info(
                "Extracted {} characters",
                extractedText.length()
        );

        List<DocumentChunk> chunks =
                chunkService.createChunks(
                        savedDocument,
                        extractedText
                );

        LOGGER.info(
                "Generated {} chunks",
                chunks.size()
        );

        documentChunkRepository.saveAll(chunks);

        embeddingService.generateEmbeddings(chunks);

        LOGGER.info(
                "Upload completed in {} ms",
                System.currentTimeMillis() - start
        );

        return new UploadResult(
                savedDocument,
                false
        );

    }
    public List<DocumentSummaryDto> getDocuments() {

        return documentRepository.findAllByOrderByUploadedAtDesc()
                .stream()
                .map(document -> new DocumentSummaryDto(

                        document.getId(),

                        document.getOriginalFileName(),

                        document.getFileType(),

                        document.getFileSize(),

                        document.getUploadedAt()

                ))
                .toList();

    }

    @Transactional
    public void delete(UUID documentId) {

        LOGGER.info("Deleting document {}", documentId);

        if (!documentRepository.existsById(documentId)) {

            throw new ValidationException(
                    "Document not found.",
                    null
            );

        }

        documentChunkRepository.deleteByDocumentId(documentId);

        documentRepository.deleteById(documentId);

        LOGGER.info("Document {} deleted successfully.", documentId);

    }

}