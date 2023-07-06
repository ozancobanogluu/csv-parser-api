package com.ozan.csvparser.service;

import com.ozan.csvparser.dto.CsvDataDto;
import com.ozan.csvparser.entity.CsvData;
import com.ozan.csvparser.repository.CsvDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ozan.csvparser.mapper.CsvDataMapper.CSV_DATA_MAPPER;


@Service
@RequiredArgsConstructor
@Slf4j
public class CsvDataServiceImpl implements CsvDataService {

    private final CsvDataRepository csvDataRepository;
    private final static Logger logger = CsvDataServiceImpl.log;

    @Override
    public void uploadData(List<CsvDataDto> csvDataList) {
        List<CsvData> entities = csvDataList.stream()
                .map(CSV_DATA_MAPPER::toEntity)
                .collect(Collectors.toList());

        csvDataRepository.saveAll(entities);
        logger.info("Data uploaded successfully. Total records: {}", csvDataList.size());
    }

    @Override
    public List<CsvDataDto> getAllData() {
        List<CsvData> allData = csvDataRepository.findAll();
        List<CsvDataDto> dtos = allData.stream()
                .map(CSV_DATA_MAPPER::toDto)
                .collect(Collectors.toList());

        logger.info("Retrieved all data. Total records: {}", dtos.size());
        return dtos;
    }

    @Override
    public Optional<CsvDataDto> getDataByCode(String code) {
        Optional<CsvData> data = csvDataRepository.findByCode(code);
        if (data.isPresent()) {
            CsvDataDto dto = CSV_DATA_MAPPER.toDto(data.get());
            logger.info("Retrieved data with code: {}", code);
            return Optional.of(dto);
        } else {
            logger.warn("No data found with code: {}", code);
            return Optional.empty();
        }
    }

    @Override
    public void deleteAllData() {
        csvDataRepository.deleteAll();
        logger.info("All data deleted.");
    }
}
