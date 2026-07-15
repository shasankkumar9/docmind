package com.ssharma.docmind.service;

import com.ssharma.docmind.config.RagProperties;
import com.ssharma.docmind.entity.Document;
import com.ssharma.docmind.entity.DocumentChunk;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ChunkService {

    private static final Pattern SENTENCE_PATTERN =
            Pattern.compile("(?<=[.!?])\\s+");

    private final RagProperties ragProperties;

    public ChunkService(RagProperties ragProperties) {
        this.ragProperties = ragProperties;
    }

    public List<DocumentChunk> createChunks(Document document,
                                            String text) {

        text = normalize(text);

        List<String> sentences = splitIntoSentences(text);

        return buildChunks(document, sentences);

    }

    private String normalize(String text) {

        return text
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .replaceAll("[ \\t]+", " ")
                .replaceAll("\\n{3,}", "\n\n")
                .trim();

    }

    private List<String> splitIntoSentences(String text) {

        return SENTENCE_PATTERN
                .splitAsStream(text)
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

    }

    private List<DocumentChunk> buildChunks(Document document,
                                            List<String> sentences) {

        List<DocumentChunk> chunks = new ArrayList<>();

        int chunkIndex = 0;
        int sentenceIndex = 0;

        while (sentenceIndex < sentences.size()) {

            StringBuilder builder = new StringBuilder();

            int startSentence = sentenceIndex;

            while (sentenceIndex < sentences.size()) {

                String sentence = sentences.get(sentenceIndex);

                if (!builder.isEmpty()
                        && builder.length() + sentence.length()
                        > ragProperties.chunkSize()) {

                    break;

                }

                builder.append(sentence).append(" ");

                sentenceIndex++;

            }

            DocumentChunk chunk = new DocumentChunk();

            chunk.setDocument(document);
            chunk.setChunkIndex(chunkIndex++);
            chunk.setContent(builder.toString().trim());

            chunks.add(chunk);

            sentenceIndex = Math.max(
                    startSentence + 1,
                    sentenceIndex - ragProperties.overlapSentences()
            );

        }

        return chunks;

    }

}