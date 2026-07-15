package com.ssharma.docmind.parser;

import com.ssharma.docmind.exception.ParsingException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class DocxParser implements DocumentParser {

    @Override
    public boolean supports(String contentType) {

        return "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                .equalsIgnoreCase(contentType);

    }

    @Override
    public String extractText(Path file) {

        try (InputStream inputStream = Files.newInputStream(file);
             XWPFDocument document = new XWPFDocument(inputStream)) {

            StringBuilder text = new StringBuilder();

            document.getParagraphs().forEach(paragraph -> {

                if (!paragraph.getText().isBlank()) {

                    text.append(paragraph.getText())
                            .append("\n");

                }

            });

            document.getTables().forEach(table -> {

                text.append("\n");

                table.getRows().forEach(row -> {

                    row.getTableCells().forEach(cell ->

                            text.append(cell.getText())
                                    .append(" | ")
                    );

                    text.append("\n");

                });

                text.append("\n");

            });

            return text.toString();

        } catch (IOException ex) {

            throw new ParsingException(
                    "Failed to parse DOCX document.",
                    ex
            );

        }

    }

}