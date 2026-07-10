package com.ssharma.docmind.parser;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class TxtParser implements DocumentParser {

    @Override
    public boolean supports(String contentType) {
        return "text/plain".equals(contentType);
    }

    @Override
    public String extractText(Path file) throws IOException {
        return Files.readString(file);
    }

}