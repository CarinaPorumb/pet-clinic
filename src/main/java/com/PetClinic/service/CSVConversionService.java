package com.PetClinic.service;

import java.io.File;
import java.util.List;

public interface CSVConversionService<T> {
    List<T> convertCSV(File csvFile);

}