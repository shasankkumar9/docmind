package com.ssharma.docmind.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.rag")
public record RagProperties(

        int chunkSize,

        int overlapSentences,

        int topK,

        double similarityThreshold

) {
}