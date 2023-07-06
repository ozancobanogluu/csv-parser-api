package com.ozan.csvparser.controller;

import com.opencsv.exceptions.CsvValidationException;
import com.ozan.csvparser.parser_exception.ResourceNotFoundException;
import com.ozan.csvparser.service.CsvDataService;
import com.ozan.csvparser.service.CsvDataServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class CsvDataController {
    private final CsvDataService csvDataService;
    private final static Logger logger = CsvDataController.log;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsvData(@RequestParam("file") MultipartFile file) {
        try {
            csvDataService.uploadData(file);
            return ResponseEntity.status(HttpStatus.OK).body("CSV file uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading CSV file");
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/fetch/all")
    public ResponseEntity<byte[]> getAllCsvData() {
        try {
            return csvDataService.getAllData();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<byte[]> getCsvDataByCode(@PathVariable String code) throws ResourceNotFoundException {
        byte[] csvBytes = csvDataService.getDataByCode(code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "entity.csv");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        logger.info("Retrieved data with code: {}", code);

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<String> deleteAllCsvData() {
        csvDataService.deleteAllData();
        return ResponseEntity.status(HttpStatus.OK).body("All CSV entity deleted successfully");
    }
}
