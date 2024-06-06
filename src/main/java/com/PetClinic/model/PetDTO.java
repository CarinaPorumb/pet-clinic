package com.PetClinic.model;

import com.PetClinic.model.enums.PetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class PetDTO {

    private UUID id;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 75)
    private String name;

    @NotNull
    private PetType petType;

    @PositiveOrZero(message = "Age must be non-negative")
    private Integer age;

    @PositiveOrZero(message = "Weight must be non-negative")
    private Double weight;

    private UUID ownerId;

    private Set<VisitDTO> visits;

    private Set<VetDTO> vets;

}