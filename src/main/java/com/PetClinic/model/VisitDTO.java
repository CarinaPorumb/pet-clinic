package com.PetClinic.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
public class VisitDTO {
    private UUID id;
    private LocalDate date;
    private String diagnosis;
    private Integer price;
    private PetDTO pet;
}