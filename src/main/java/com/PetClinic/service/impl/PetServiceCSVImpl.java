package com.PetClinic.service.impl;

import com.PetClinic.model.PetCSVRecord;
import com.PetClinic.service.PetServiceCSV;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

@Service
public class PetServiceCSVImpl implements PetServiceCSV {

    @Override
    public List<PetCSVRecord> convertCSV(File csvFile) {
        try {
            return new CsvToBeanBuilder<PetCSVRecord>(new FileReader(csvFile))
                    .withType(PetCSVRecord.class)
                    .build().parse();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}