package com.ssharma.docmind.service;

import com.ssharma.docmind.entity.Document;
import com.ssharma.docmind.entity.DocumentChunk;
import com.ssharma.docmind.parser.DocumentParser;
import com.ssharma.docmind.parser.ParserService;
import com.ssharma.docmind.repository.DocumentChunkRepository;
import com.ssharma.docmind.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Value;
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

    private final DocumentRepository documentRepository;
    private final DocumentChunkRepository documentChunkRepository;
    private final ParserService parserService;
    private final ChunkService chunkService;
    private final EmbeddingService embeddingService;

    @Value("${app.upload.directory}")
    private String uploadDirectory;

    public DocumentService(DocumentRepository documentRepository,
                           DocumentChunkRepository documentChunkRepository,
                           ParserService parserService,
                           ChunkService chunkService,
                           EmbeddingService embeddingService) {

        this.documentRepository = documentRepository;
        this.documentChunkRepository = documentChunkRepository;
        this.parserService = parserService;
        this.chunkService = chunkService;
        this.embeddingService = embeddingService;
    }

    public Document upload(MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(uploadDirectory);

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

        DocumentParser parser = parserService.getParser(file.getContentType());

        String extractedText = parser.extractText(destination);

        List<DocumentChunk> documentChunks =
                chunkService.createChunks(savedDocument, extractedText);

        documentChunkRepository.saveAll(documentChunks);

        embeddingService.generateEmbeddings(documentChunks);

        return savedDocument;
    }

}