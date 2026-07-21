package com.ssharma.docmind.dto;

import com.ssharma.docmind.entity.Document;

public record UploadResult(
        Document document,
        boolean alreadyExists
) {
}
