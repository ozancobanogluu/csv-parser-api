package com.ozan.csvparser.service;

import com.ozan.csvparser.dto.CsvDataDto;
import com.ozan.csvparser.entity.CsvData;
import com.ozan.csvparser.mapper.CsvDataMapper;
import com.ozan.csvparser.repository.CsvDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CsvDataServiceImplTest {

    @Mock
    private CsvDataRepository csvDataRepository;

    @InjectMocks
    private CsvDataServiceImpl csvDataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadData_ShouldSaveEntitiesAndReturnCorrectCount() {
        // Arrange
        List<CsvDataDto> csvDataList = new ArrayList<>();
        csvDataList.add(new CsvDataDto("source1", "codeListCode1", "code1", "displayValue1",
                "longDescription1", "fromDate1", "toDate1", "sortingPriority1"));
        csvDataList.add(new CsvDataDto("source2", "codeListCode2", "code2", "displayValue2",
                "longDescription2", "fromDate2", "toDate2", "sortingPriority2"));

        List<CsvData> expectedEntities = CsvDataMapper.CSV_DATA_MAPPER.toEntityList(csvDataList);

        // Act
        csvDataService.uploadData(csvDataList);

        // Assert
        verify(csvDataRepository, times(1)).saveAll(expectedEntities);
    }

    @Test
    void getAllData_ShouldReturnAllDataAsDtos() {
        // Arrange
        List<CsvData> mockData = new ArrayList<>();
        mockData.add(new CsvData(1, "source1", "codeListCode1", "code1", "displayValue1",
                "longDescription1", "fromDate1", "toDate1", "sortingPriority1"));
        mockData.add(new CsvData(2, "source2", "codeListCode2", "code2", "displayValue2",
                "longDescription2", "fromDate2", "toDate2", "sortingPriority2"));

        when(csvDataRepository.findAll()).thenReturn(mockData);

        List<CsvDataDto> expectedDtos = CsvDataMapper.CSV_DATA_MAPPER.toDtoList(mockData);

        // Act
        List<CsvDataDto> result = csvDataService.getAllData();

        // Assert
        assertEquals(expectedDtos.size(), result.size());
        for (int i = 0; i < expectedDtos.size(); i++) {
            CsvDataDto expectedDto = expectedDtos.get(i);
            CsvDataDto actualDto = result.get(i);
            assertEquals(expectedDto.getCode(), actualDto.getCode());
            // Add assertions for other fields
        }
    }

    @Test
    void getDataByCode_WithExistingCode_ShouldReturnDto() {
        // Arrange
        String code = "code1";
        CsvData mockData = new CsvData(
                1, "source1", "codeListCode1", "code1", "displayValue1",
                "longDescription1", "fromDate1", "toDate1", "sortingPriority1");

        when(csvDataRepository.findByCode(code)).thenReturn(Optional.of(mockData));

        CsvDataDto expectedDto = CsvDataMapper.CSV_DATA_MAPPER.toDto(mockData);

        // Act
        Optional<CsvDataDto> result = csvDataService.getDataByCode(code);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedDto.getCode(), result.get().getCode());
        // Add assertions for other fields
    }

    @Test
    void getDataByCode_WithNonExistingCode_ShouldReturnEmptyOptional() {
        // Arrange
        String code = "nonExistingCode";
        when(csvDataRepository.findByCode(code)).thenReturn(Optional.empty());

        // Act
        Optional<CsvDataDto> result = csvDataService.getDataByCode(code);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void deleteAllData_ShouldCallRepositoryDeleteAll() {
        // Act
        csvDataService.deleteAllData();

        // Assert
        verify(csvDataRepository, times(1)).deleteAll();
    }
}