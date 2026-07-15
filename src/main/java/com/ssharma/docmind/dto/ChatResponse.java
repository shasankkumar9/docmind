package com.ssharma.docmind.dto;

import java.util.List;

public record ChatResponse(

        String response,

        List<SourceDto> sources

) {
}