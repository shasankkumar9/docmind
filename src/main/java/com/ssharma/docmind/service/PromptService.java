package com.ssharma.docmind.service;

import com.ssharma.docmind.dto.RetrievedChunk;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromptService {

    public String buildPrompt(List<RetrievedChunk> chunks,
                              String question) {

        StringBuilder context = new StringBuilder();

        for (RetrievedChunk chunk : chunks) {

            context.append(chunk.chunk().getContent())
                    .append("\n\n");

        }

        return """
                You are an AI assistant answering questions from an uploaded document.
                
                Rules:
                
                - Answer ONLY using the supplied context.
                - If the answer is not present, reply exactly:
                  "I couldn't find any relevant information in the uploaded document."
                - Do not hallucinate.
                - Keep the answer concise.
                
                Context:
                
                %s
                
                Question:
                
                %s
                """.formatted(context, question);

    }

}