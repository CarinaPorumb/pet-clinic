package com.PetClinic.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
public class VisitDTO {

    private UUID id;

    @NotBlank(message = "Diagnosis must not be blank")
    private String diagnosis;

    @PositiveOrZero(message = "Price must be non-negative")
    private Integer price;

    private LocalDate date;

    private PetDTO pet;

}