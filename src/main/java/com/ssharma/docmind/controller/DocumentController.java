package com.ssharma.docmind.controller;

import com.ssharma.docmind.dto.DocumentSummaryDto;
import com.ssharma.docmind.dto.UploadResponse;
import com.ssharma.docmind.entity.Document;
import com.ssharma.docmind.service.DocumentService;
import com.ssharma.docmind.swagger.UploadApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    public UploadResponse upload(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        Document document = documentService.upload(file);

        return new UploadResponse(
                document.getId(),
                document.getOriginalFileName(),
                "Document uploaded successfully."
        );

    }

    @GetMapping
    public List<DocumentSummaryDto> getDocuments() {

        return documentService.getDocuments();

    }

}