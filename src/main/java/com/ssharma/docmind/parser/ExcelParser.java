package com.ssharma.docmind.parser;

import com.ssharma.docmind.exception.ParsingException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class ExcelParser implements DocumentParser {

    @Override
    public boolean supports(String contentType) {

        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                .equalsIgnoreCase(contentType);

    }

    @Override
    public String extractText(Path file) {

        try (InputStream inputStream = Files.newInputStream(file);
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            DataFormatter formatter = new DataFormatter();

            StringBuilder text = new StringBuilder();

            for (Sheet sheet : workbook) {

                text.append("Sheet: ")
                        .append(sheet.getSheetName())
                        .append("\n\n");

                for (Row row : sheet) {

                    for (Cell cell : row) {

                        text.append(
                                formatter.formatCellValue(cell)
                        ).append(" | ");

                    }

                    text.append("\n");

                }

                text.append("\n");

            }

            return text.toString();

        } catch (IOException ex) {

            throw new ParsingException(
                    "Failed to parse Excel workbook.",
                    ex
            );

        }

    }

}