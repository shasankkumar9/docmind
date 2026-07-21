package com.ssharma.docmind.controller;

import com.ssharma.docmind.dto.DocumentSummaryDto;
import com.ssharma.docmind.dto.UploadResponse;
import com.ssharma.docmind.dto.UploadResult;
import com.ssharma.docmind.entity.Document;
import com.ssharma.docmind.service.DocumentService;
import com.ssharma.docmind.swagger.UploadApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(
        name = "Documents",
        description = "Document upload and indexing"
)
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Operation(
            summary = "Upload a document",
            description = """
                    Uploads a supported document,
                    extracts its contents,
                    creates sentence-aware chunks,
                    generates vector embeddings,
                    and indexes the document for semantic search.
                    """
    )
    @UploadApiResponses
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> upload(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        UploadResult result =
                documentService.upload(file);

        Document document = result.document();

        String message =
                result.alreadyExists()
                        ? "Document already exists."
                        : "Document uploaded successfully.";

        return ResponseEntity.ok(
                new UploadResponse(
                        document.getId(),
                        document.getOriginalFileName(),
                        result.alreadyExists(),
                        message
                )
        );
    }

    @GetMapping
    public List<DocumentSummaryDto> getDocuments() {

        return documentService.getDocuments();

    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable UUID documentId) {

        documentService.delete(documentId);

        return ResponseEntity.noContent().build();

    }

}