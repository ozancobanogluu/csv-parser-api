package com.ozan.csvparser.service;

import com.opencsv.exceptions.CsvValidationException;
import com.ozan.csvparser.dto.CsvDataDto;
import com.ozan.csvparser.entity.CsvData;
import com.ozan.csvparser.parser_exception.ResourceNotFoundException;
import com.ozan.csvparser.repository.CsvDataRepository;
import com.ozan.csvparser.utility.CsvDataParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ozan.csvparser.mapper.CsvDataMapper.CSV_DATA_MAPPER;


@Service
@RequiredArgsConstructor
@Slf4j
public class CsvDataServiceImpl implements CsvDataService {

    private final CsvDataRepository csvDataRepository;
    private final CsvDataParser csvDataParser;

    private final static Logger logger = CsvDataServiceImpl.log;

    @Override
    public void uploadData(MultipartFile file) throws CsvValidationException, IOException {
        List<CsvDataDto> csvDataDtoList = csvDataParser.parseCsvFile(file);
        List<CsvData> entities = csvDataDtoList.stream()
                .map(CSV_DATA_MAPPER::toEntity)
                .collect(Collectors.toList());

        csvDataRepository.saveAll(entities);
        logger.info("Data uploaded successfully. Total records: {}", entities.size());
    }

    @Override
    public ResponseEntity<byte[]> getAllData() {
        List<CsvData> allData = csvDataRepository.findAll();

        List<CsvDataDto> dtos = Optional.of(allData.stream()
                .map(CSV_DATA_MAPPER::toDto)
                .collect(Collectors.toList()))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "There is no data in database."
                ));

        byte[] csvBytes = csvDataParser.convertToCsvBytes(dtos);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "entity.csv");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        logger.info("Retrieved all data. Total records: {}", dtos.size());
        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

    @Override
    public byte[] getDataByCode(String code) throws ResourceNotFoundException {
        return csvDataRepository.findByCode(code)
                .map(CSV_DATA_MAPPER::toDto)
                .map(data -> csvDataParser.convertToCsvBytes(Collections.singletonList(data)))
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Csv data with code %s not found.",code)
                ));
    }

    @Override
    public void deleteAllData() {
        csvDataRepository.deleteAll();
        logger.info("All data deleted.");
    }
}
