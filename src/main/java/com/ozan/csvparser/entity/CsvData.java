package com.ozan.csvparser.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CsvData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String source;
    private String codeListCode;
    private String code;
    private String displayValue;
    private String longDescription;
    private String fromDate;
    private String toDate;
    private String sortingPriority;

    // toString method for debugging purposes
    @Override
    public String toString() {
        return "CsvData{" +
                "source='" + source + '\'' +
                ", codeListCode='" + codeListCode + '\'' +
                ", code='" + code + '\'' +
                ", displayValue='" + displayValue + '\'' +
                ", longDescription='" + longDescription + '\'' +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", sortingPriority='" + sortingPriority + '\'' +
                '}';
    }
}
