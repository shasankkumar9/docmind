package com.ssharma.docmind.parser;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParserService {

    private final List<DocumentParser> parsers;

    public ParserService(List<DocumentParser> parsers) {
        this.parsers = parsers;
    }

    public DocumentParser getParser(String contentType) {

        return parsers.stream()
                .filter(parser -> parser.supports(contentType))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unsupported file type: " + contentType));

    }

}