package com.PetClinic.service.impl;

import com.PetClinic.model.PetCSVRecord;
import com.PetClinic.service.PetServiceCSV;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PetServiceCSVImplTest {

    PetServiceCSV petServiceCSV = new PetServiceCSVImpl();

    @BeforeEach
    void setUp() {
    }

    @Test
    void convertCSV() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/pet.csv");
        List<PetCSVRecord> records = petServiceCSV.convertCSV(file);
        System.out.println(records.size());
        assertThat(records.size()).isGreaterThan(0);
    }
}