package com.ssharma.docmind.controller;

import com.ssharma.docmind.dto.UploadDocumentResponse;
import com.ssharma.docmind.entity.Document;
import com.ssharma.docmind.service.DocumentService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadDocumentResponse upload(
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        Document document = documentService.upload(file);

        return new UploadDocumentResponse(
                document.getId(),
                document.getOriginalFileName(),
                "Document uploaded successfully."
        );

    }

}