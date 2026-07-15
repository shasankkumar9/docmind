package com.ssharma.docmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Source citation"
)
public record SourceDto(

        @Schema(example = "17")
        int chunkIndex,

        @Schema(example = "0.85")
        double similarity,

        @Schema(example = "This is a preview of the source content.")
        String preview

) {
}