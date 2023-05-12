package com.PetClinic.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class PetCSVRecord {

    @CsvBindByName
    private String name;

    @CsvBindByName(column = "type")
    private String petType;

    @CsvBindByName
    private Integer age;

    @CsvBindByName
    private Double weight;
}