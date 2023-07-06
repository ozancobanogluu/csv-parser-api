package com.ozan.csvparser.mapper;

import com.ozan.csvparser.dto.CsvDataDto;
import com.ozan.csvparser.entity.CsvData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CsvDataMapper {
    CsvDataMapper CSV_DATA_MAPPER = Mappers.getMapper(CsvDataMapper.class);

    CsvDataDto toDto(CsvData csvData);

    CsvData toEntity(CsvDataDto csvDataDto);

    List<CsvDataDto> toDtoList(List<CsvData> csvDataList);

    List<CsvData> toEntityList(List<CsvDataDto> csvDataDtoList);
}
