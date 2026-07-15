package com.ssharma.docmind.dto;

import com.ssharma.docmind.entity.DocumentChunk;

public record RetrievedChunk(
        DocumentChunk chunk,
        double distance
) {

    public double similarity() {
        return 1.0 - distance;
    }

}