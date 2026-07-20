package com.ssharma.docmind.service.retrieval;

import org.springframework.stereotype.Component;

@Component
public class RetrievalModeDetector {

    public RetrievalMode detect(String question) {

        String query = question.toLowerCase();

        if (query.contains("summar")
                || query.contains("summary")
                || query.contains("overview")
                || query.contains("review")
                || query.contains("analyze")
                || query.contains("describe the document")
                || query.contains("describe this document")) {

            return RetrievalMode.FULL_DOCUMENT;

        }

        return RetrievalMode.SEMANTIC;

    }

}