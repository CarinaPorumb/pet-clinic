package com.PetClinic.model;

import com.PetClinic.model.enums.PetType;
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
    private String name;
    private PetType petType;
    private OwnerDTO ownerDTO;
    private LocalDate birthDate;
    @Builder.Default
    private Set<VisitDTO> visitDTOSet = new HashSet<>();
}