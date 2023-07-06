package com.ozan.csvparser.utility;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import com.ozan.csvparser.dto.CsvDataDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvDataParser {
    private static final int EXPECTED_FIELD_COUNT = 8;
    private static final Logger logger = LoggerFactory.getLogger(CsvDataParser.class);

    public List<CsvDataDto> parseCsvFile(MultipartFile file) throws IOException, CsvValidationException {
        List<CsvDataDto> csvDataDtoList = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] line;
            boolean isFirstLine = true;
            int lineCount = 0;

            while ((line = reader.readNext()) != null) {
                lineCount++;

                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the header line
                }

                if (line.length != EXPECTED_FIELD_COUNT) {
                    // Handle invalid line or mismatched field count
                    logger.error("Invalid line at line number {}: {}", lineCount, String.join(",", line));
                    continue;
                }

                try {
                    CsvDataDto csvDataDto = createCsvData(line);
                    csvDataDtoList.add(csvDataDto);
                } catch (Exception e) {
                    // Handle error while creating CsvData object
                    logger.error("Error parsing line at line number {}: {}", lineCount, String.join(",", line));
                    logger.error("Exception: {}", e.getMessage());
                }
            }
        }

        return csvDataDtoList;
    }

    public byte[] convertToCsvBytes(List<CsvDataDto> csvDataList) {
        try (StringWriter writer = new StringWriter(); CSVWriter csvWriter = new CSVWriter(writer)) {
            csvWriter.writeNext(getCsvHeaders());

            for (CsvDataDto csvDataDto : csvDataList) {
                String[] fields = getCsvFields(csvDataDto);
                csvWriter.writeNext(fields);
            }

            csvWriter.flush();
            return writer.toString().getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Error converting CsvData to CSV bytes", e);
        }
    }

    private String[] getCsvHeaders() {
        return new String[]{"source", "codeListCode", "code", "displayValue", "longDescription", "fromDate", "toDate", "sortingPriority"};
    }

    private String[] getCsvFields(CsvDataDto csvDataDto) {
        return new String[]{
                csvDataDto.getSource(),
                csvDataDto.getCodeListCode(),
                csvDataDto.getCode(),
                csvDataDto.getDisplayValue(),
                csvDataDto.getLongDescription(),
                csvDataDto.getFromDate(),
                csvDataDto.getToDate(),
                csvDataDto.getSortingPriority()
        };
    }

    private CsvDataDto createCsvData(String[] fields) {
        String source = fields[0];
        String codeListCode = fields[1];
        String code = fields[2];
        String displayValue = fields[3];
        String longDescription = fields[4];
        String fromDate = fields[5];
        String toDate = fields[6];
        String sortingPriority = fields[7];

        return new CsvDataDto(source, codeListCode, code, displayValue, longDescription, fromDate, toDate, sortingPriority);
    }
}
