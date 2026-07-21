package com.ssharma.docmind.dto;

import java.util.UUID;

public record UploadResponse(

        UUID id,

        String originalFileName,

        boolean alreadyExists,

        String message

) {
}