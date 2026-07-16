package com.ssharma.docmind.parser;

import com.ssharma.docmind.exception.ParsingException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class TextParser implements DocumentParser {

    @Override
    public boolean supports(String contentType) {

        return "text/plain".equalsIgnoreCase(contentType);

    }

    @Override
    public String extractText(MultipartFile file) {

        try {

            return new String(
                    file.getBytes(),
                    StandardCharsets.UTF_8
            );

        } catch (IOException ex) {

            throw new ParsingException(
                    "Failed to parse text document.",
                    ex
            );

        }

    }

}