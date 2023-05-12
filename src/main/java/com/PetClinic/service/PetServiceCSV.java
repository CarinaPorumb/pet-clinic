package com.PetClinic.service;

import com.PetClinic.model.PetCSVRecord;

import java.io.File;
import java.util.List;

public interface PetServiceCSV {

    List<PetCSVRecord> convertCSV(File csvFile);
}
