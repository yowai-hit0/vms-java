package com.rw.rra.vms.files;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelService {

    public byte[] generateExcel(String sheetName,
                                List<String> headers,
                                List<List<String>> rows) throws IOException {
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet(sheetName);

        // header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
        }

        // data rows
        for (int i = 0; i < rows.size(); i++) {
            Row r = sheet.createRow(i + 1);
            List<String> rowData = rows.get(i);
            for (int j = 0; j < rowData.size(); j++) {
                r.createCell(j).setCellValue(rowData.get(j));
            }
        }

        // autosize
        for (int i = 0; i < headers.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        wb.write(out);
        wb.close();
        return out.toByteArray();
    }
}
