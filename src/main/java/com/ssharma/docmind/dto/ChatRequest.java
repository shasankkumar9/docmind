package com.ssharma.docmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Chat request")
public class ChatRequest {

    @Schema(
            description = "Unique identifier of the uploaded document",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    @NotNull
    private UUID documentId;

    @Schema(
            description = "Question to ask about the document",
            example = "What OCR technologies do I know?"
    )
    @NotBlank
    private String message;

    public ChatRequest() {
    }

    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}