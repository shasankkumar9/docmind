package com.ssharma.docmind.parser;

import com.ssharma.docmind.exception.ParsingException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class DocxParser implements DocumentParser {

    @Override
    public boolean supports(String contentType) {

        return "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                .equalsIgnoreCase(contentType);

    }

    @Override
    public String extractText(MultipartFile file) {

        try (XWPFDocument document =
                     new XWPFDocument(file.getInputStream())) {

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