package com.ssharma.docmind.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentSummaryDto(

        UUID id,

        String originalFileName,

        String fileType,

        Long fileSize,

        LocalDateTime uploadedAt

) {
}