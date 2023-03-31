package com.PetClinic.model;

import com.PetClinic.model.enums.PetType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
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

    private OwnerDTO ownerDTO;

    private LocalDate birthDate;

    @Builder.Default
    private Set<VisitDTO> visitDTOSet = new HashSet<>();
}