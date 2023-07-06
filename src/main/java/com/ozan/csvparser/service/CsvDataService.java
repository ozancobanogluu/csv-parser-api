package com.ozan.csvparser.service;

import com.opencsv.exceptions.CsvValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CsvDataService {
    void uploadData(MultipartFile file) throws CsvValidationException, IOException;

    ResponseEntity<byte[]> getAllData();

    byte[] getDataByCode(String code);

    void deleteAllData();
}
