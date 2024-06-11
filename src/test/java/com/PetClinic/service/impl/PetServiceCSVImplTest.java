//package com.PetClinic.service.impl;
//
//import com.PetClinic.model.PetCSVRecord;
//import com.PetClinic.service.CSVConversionService;
//import org.junit.jupiter.api.Test;
//import org.springframework.util.ResourceUtils;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class PetServiceCSVImplTest {
//
//    CSVConversionService petServiceCSV = new CSVConversionServiceImpl();
//
//    @Test
//    void convertCSV() throws FileNotFoundException {
//        File file = ResourceUtils.getFile("classpath:csvdata/pet.csv");
//        List<PetCSVRecord> records = petServiceCSV.convertCSV(file);
//        System.out.println(records.size());
//        assertThat(records.size()).isGreaterThan(0);
//    }
//
//}