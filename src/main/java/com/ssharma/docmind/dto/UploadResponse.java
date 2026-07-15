package com.ssharma.docmind.dto;

import java.util.UUID;

public class UploadResponse {

    private UUID id;
    private String originalFileName;
    private String message;

    public UploadResponse() {
    }

    public UploadResponse(UUID id, String originalFileName, String message) {
        this.id = id;
        this.originalFileName = originalFileName;
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}