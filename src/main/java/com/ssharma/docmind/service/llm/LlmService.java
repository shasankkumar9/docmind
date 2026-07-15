package com.ssharma.docmind.service.llm;

public interface LlmService {

    /**
     * Generates a response for the supplied prompt.
     */
    String generate(String prompt);

}