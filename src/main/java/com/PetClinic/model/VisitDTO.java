package com.PetClinic.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
public class VisitDTO {

    private UUID id;

    @NotNull
    @NotBlank
    private String diagnosis;

    private Integer price;

    private LocalDate date;

    private PetDTO pet;


}