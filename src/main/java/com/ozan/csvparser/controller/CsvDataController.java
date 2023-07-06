package com.ozan.csvparser.controller;

import com.opencsv.exceptions.CsvValidationException;
import com.ozan.csvparser.dto.CsvDataDto;
import com.ozan.csvparser.service.CsvDataService;
import com.ozan.csvparser.utility.CsvDataParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CsvDataController {
    private final CsvDataService csvDataService;
    private final CsvDataParser csvDataParser;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsvData(@RequestParam("file") MultipartFile file) {
        try {
            List<CsvDataDto> csvDataDtoList = csvDataParser.parseCsvFile(file);
            csvDataService.uploadData(csvDataDtoList);
            return ResponseEntity.status(HttpStatus.OK).body("CSV file uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading CSV file");
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<byte[]> getAllCsvData() {
        List<CsvDataDto> allData = csvDataService.getAllData();
        byte[] csvBytes = csvDataParser.convertToCsvBytes(allData);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "entity.csv");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<byte[]> getCsvDataByCode(@PathVariable String code) {
        Optional<CsvDataDto> csvData = csvDataService.getDataByCode(code);
        if (csvData.isPresent()) {
            CsvDataDto singleData = csvData.get();
            byte[] csvBytes = csvDataParser.convertToCsvBytes(Collections.singletonList(singleData));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentDispositionFormData("attachment", "entity.csv");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<String> deleteAllCsvData() {
        csvDataService.deleteAllData();
        return ResponseEntity.status(HttpStatus.OK).body("All CSV entity deleted successfully");
    }
}
