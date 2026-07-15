package com.ssharma.docmind.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(
        description = "Chat response"
)
public record ChatResponse(

        @Schema(
                description = "Generated answer"
        )
        String response,

        @Schema(
                description = "Retrieved document sources"
        )
        List<SourceDto> sources

) {
}