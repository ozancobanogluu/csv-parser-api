package com.ozan.csvparser.service;

import com.opencsv.exceptions.CsvValidationException;
import com.ozan.csvparser.dto.CsvDataDto;
import com.ozan.csvparser.entity.CsvData;
import com.ozan.csvparser.mapper.CsvDataMapper;
import com.ozan.csvparser.parser_exception.ResourceNotFoundException;
import com.ozan.csvparser.repository.CsvDataRepository;
import com.ozan.csvparser.utility.CsvDataParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CsvDataServiceImplTest {

    @Mock
    private CsvDataRepository csvDataRepository;

    @Mock
    private CsvDataParser csvDataParser;

    @Mock
    private CsvDataMapper csvDataMapper;

    @InjectMocks
    private CsvDataServiceImpl csvDataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadData_ShouldSaveEntitiesAndLogInfo() throws IOException, CsvValidationException {
        // Arrange
        MultipartFile file = new MockMultipartFile("test.csv", new byte[10]);

        List<CsvDataDto> csvDataDtoList = Collections.singletonList(new CsvDataDto());
        List<CsvData> entities = Collections.singletonList(new CsvData());

        when(csvDataParser.parseCsvFile(file)).thenReturn(csvDataDtoList);
        when(csvDataMapper.toEntity(any(CsvDataDto.class))).thenReturn(new CsvData());

        // Act
        csvDataService.uploadData(file);

        // Assert
        verify(csvDataRepository, times(1)).saveAll(entities);
        verify(csvDataParser, times(1)).parseCsvFile(file);
    }

    @Test
    void getAllData_ShouldReturnCsvDataAsResponseEntity() {
        // Arrange
        List<CsvData> allData = Arrays.asList(new CsvData(), new CsvData());
        List<CsvDataDto> dtos = Arrays.asList(new CsvDataDto(), new CsvDataDto());

        when(csvDataRepository.findAll()).thenReturn(allData);
        when(csvDataMapper.toDto(any(CsvData.class))).thenReturn(new CsvDataDto());
        when(csvDataParser.convertToCsvBytes(dtos)).thenReturn(new byte[0]);

        // Act
        ResponseEntity<byte[]> response = csvDataService.getAllData();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.parseMediaType("text/csv"), response.getHeaders().getContentType());
        assertEquals("form-data; name=\"attachment\"; filename=\"entity.csv\"", response.getHeaders().getContentDisposition().toString());
        verify(csvDataRepository, times(1)).findAll();
        verify(csvDataParser, times(1)).convertToCsvBytes(dtos);
    }

    @Test
    void getAllData_WithEmptyData_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(csvDataRepository.findAll()).thenThrow(new ResourceNotFoundException("Error"));

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> csvDataService.getAllData());
        verify(csvDataRepository, times(1)).findAll();
    }

    @Test
    void getDataByCode_WithExistingCode_ShouldReturnCsvDataAsResponseEntity() throws ResourceNotFoundException {
        // Arrange
        String code = "code1";

        CsvData csvData = new CsvData();
        CsvDataDto csvDataDto = new CsvDataDto();

        when(csvDataRepository.findByCode(code)).thenReturn(Optional.of(csvData));
        when(csvDataMapper.toDto(csvData)).thenReturn(csvDataDto);
        when(csvDataParser.convertToCsvBytes(Collections.singletonList(csvDataDto))).thenReturn(new byte[10]);

        // Act
        byte[] response = csvDataService.getDataByCode(code);

        // Assert
        assertTrue(response.length != 0);
        verify(csvDataRepository, times(1)).findByCode(code);
        verify(csvDataParser, times(1)).convertToCsvBytes(Collections.singletonList(csvDataDto));
    }

    @Test
    void getDataByCode_WithNonExistingCode_ShouldThrowResourceNotFoundException() {
        // Arrange
        String code = "nonExistingCode";

        when(csvDataRepository.findByCode(code)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () -> csvDataService.getDataByCode(code));
        verify(csvDataRepository, times(1)).findByCode(code);
        verify(csvDataParser, never()).convertToCsvBytes(anyList());
    }

    @Test
    void deleteAllData_ShouldCallRepositoryDeleteAllAndLogInfo() {
        // Act
        csvDataService.deleteAllData();

        // Assert
        verify(csvDataRepository, times(1)).deleteAll();
    }
}
