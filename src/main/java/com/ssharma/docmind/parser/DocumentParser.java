package com.ssharma.docmind.parser;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DocumentParser {

    boolean supports(String contentType);

    String extractText(MultipartFile file) throws IOException;

}