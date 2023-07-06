package com.ozan.csvparser.repository;

import com.ozan.csvparser.entity.CsvData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CsvDataRepository extends JpaRepository<CsvData, Integer> {
    Optional<CsvData> findByCode(String code);
}
