package com.ssharma.docmind.dto;

public record SourceDto(

        int chunkIndex,

        double similarity,

        String preview

) {
}