package com.ssharma.docmind.service;

import com.ssharma.docmind.entity.DocumentChunk;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromptService {

    public String buildPrompt(String question,
                              List<DocumentChunk> chunks) {

        String context = chunks.stream()
                .map(DocumentChunk::getContent)
                .collect(Collectors.joining("\n\n"));

        return """
                You are a document question-answering assistant.
                
                Use ONLY the provided context.
                
                Do NOT use your own knowledge.
                
                If the answer is not present in the context,
                respond exactly with:
                
                I couldn't find that information in the uploaded documents.
                
                ------------------------
                CONTEXT
                ------------------------
                
                %s
                
                ------------------------
                QUESTION
                ------------------------
                
                %s
                
                ------------------------
                ANSWER
                ------------------------
                """.formatted(context, question);

    }

}