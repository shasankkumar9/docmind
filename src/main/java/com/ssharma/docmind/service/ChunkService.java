package com.ssharma.docmind.service;

import com.ssharma.docmind.entity.Document;
import com.ssharma.docmind.entity.DocumentChunk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class ChunkService {

    @Value("${app.chunk.size}")
    private int chunkSize;

    @Value("${app.chunk.overlap}")
    private int chunkOverlap;

    public List<DocumentChunk> createChunks(Document document, String text) {

        if (text == null || text.isBlank()) {
            return List.of();
        }

        List<String> chunks = splitText(text);

        return IntStream.range(0, chunks.size())
                .mapToObj(index -> {

                    DocumentChunk chunk = new DocumentChunk();

                    chunk.setDocument(document);
                    chunk.setChunkIndex(index);
                    chunk.setContent(chunks.get(index));

                    return chunk;

                })
                .toList();
    }

    private List<String> splitText(String text) {

        List<String> chunks = new java.util.ArrayList<>();

        int start = 0;

        while (start < text.length()) {

            int end = Math.min(start + chunkSize, text.length());

            chunks.add(text.substring(start, end));

            if (end >= text.length()) {
                break;
            }

            start = end - chunkOverlap;
        }

        return chunks;
    }

}