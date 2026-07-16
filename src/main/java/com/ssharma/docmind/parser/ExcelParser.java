package com.ssharma.docmind.parser;

import com.ssharma.docmind.exception.ParsingException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class ExcelParser implements DocumentParser {

    @Override
    public boolean supports(String contentType) {

        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                .equalsIgnoreCase(contentType);

    }

    @Override
    public String extractText(MultipartFile file) {

        try (Workbook workbook =
                     new XSSFWorkbook(file.getInputStream())) {

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