package com.ozan.csvparser.service;

import com.ozan.csvparser.dto.CsvDataDto;

import java.util.List;
import java.util.Optional;

public interface CsvDataService {
    void uploadData(List<CsvDataDto> csvDataDtoList);

    List<CsvDataDto> getAllData();

    Optional<CsvDataDto> getDataByCode(String code);

    void deleteAllData();
}
