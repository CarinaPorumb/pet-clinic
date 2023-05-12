package com.PetClinic.model;

import com.PetClinic.model.enums.PetType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class PetDTO {

    private UUID id;

    @NotNull
    @NotBlank
    @Size(max = 75)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PetType petType;

    @NotNull
    private Integer age;

    @NotNull
    private Double weight;

    private OwnerDTO owner;

    private VisitDTO visit;

    private Set<VetDTO> vets;

}