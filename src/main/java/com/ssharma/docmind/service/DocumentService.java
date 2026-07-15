package com.ssharma.docmind.service;

import com.ssharma.docmind.config.StorageProperties;
import com.ssharma.docmind.entity.Document;
import com.ssharma.docmind.entity.DocumentChunk;
import com.ssharma.docmind.exception.ParsingException;
import com.ssharma.docmind.exception.ValidationException;
import com.ssharma.docmind.parser.DocumentParser;
import com.ssharma.docmind.parser.ParserService;
import com.ssharma.docmind.repository.DocumentChunkRepository;
import com.ssharma.docmind.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
    private final StorageProperties storageProperties;

    public DocumentService(DocumentRepository documentRepository,
                           DocumentChunkRepository documentChunkRepository,
                           ParserService parserService,
                           ChunkService chunkService,
                           EmbeddingService embeddingService,
                           StorageProperties storageProperties) {

        this.documentRepository = documentRepository;
        this.documentChunkRepository = documentChunkRepository;
        this.parserService = parserService;
        this.chunkService = chunkService;
        this.embeddingService = embeddingService;
        this.storageProperties = storageProperties;
    }

    public Document upload(MultipartFile file) {

        long start = System.currentTimeMillis();

        if (file.isEmpty()) {
            throw new ValidationException("Uploaded file is empty.", null);
        }

        try {

            LOGGER.info("Uploading document: {}", file.getOriginalFilename());

            Path uploadPath = Paths.get(storageProperties.directory());

            Files.createDirectories(uploadPath);

            String originalFileName = file.getOriginalFilename();

            String extension = "";

            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            String storedFileName = UUID.randomUUID() + extension;

            Path destination = uploadPath.resolve(storedFileName);

            Files.copy(
                    file.getInputStream(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );

            Document document = new Document();

            document.setOriginalFileName(originalFileName);
            document.setStoredFileName(storedFileName);
            document.setFileType(file.getContentType());
            document.setFileSize(file.getSize());
            document.setFilePath(destination.toString());
            document.setUploadedAt(LocalDateTime.now(Clock.systemDefaultZone()));

            Document savedDocument = documentRepository.save(document);

            LOGGER.info("Document saved with id={}", savedDocument.getId());

            DocumentParser parser =
                    parserService.getParser(file.getContentType());

            String extractedText =
                    parser.extractText(destination);

            LOGGER.info("Extracted {} characters",
                    extractedText.length());

            List<DocumentChunk> documentChunks =
                    chunkService.createChunks(
                            savedDocument,
                            extractedText
                    );

            LOGGER.info("Generated {} chunks",
                    documentChunks.size());

            documentChunkRepository.saveAll(documentChunks);

            embeddingService.generateEmbeddings(documentChunks);

            LOGGER.info(
                    "Upload completed in {} ms",
                    System.currentTimeMillis() - start
            );

            return savedDocument;

        } catch (IOException ex) {

            throw new ParsingException(
                    "Failed to process uploaded document.",
                    ex
            );

        }

    }

}