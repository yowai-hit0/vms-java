package com.rw.rra.vms.files;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CsvService {

    public byte[] generateCsv(List<String> headers,
                              List<List<String>> rows) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (CSVPrinter printer = new CSVPrinter(
                new OutputStreamWriter(baos, StandardCharsets.UTF_8),
                CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0]))
        )) {
            for (List<String> row : rows) {
                printer.printRecord(row);
            }
        }
        return baos.toByteArray();
    }
}
