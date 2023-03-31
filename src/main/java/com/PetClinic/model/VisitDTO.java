package com.PetClinic.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
public class VisitDTO {

    private UUID id;

    private LocalDate date;

    @NotNull
    @NotBlank
    @Size(max = 250)
    private String diagnosis;

    private Integer price;

    private PetDTO pet;
}