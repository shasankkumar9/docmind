package com.ssharma.docmind.parser;

import java.io.IOException;
import java.nio.file.Path;

public interface DocumentParser {

    boolean supports(String contentType);

    String extractText(Path file) throws IOException;

}