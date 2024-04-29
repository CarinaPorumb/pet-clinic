package com.PetClinic.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class OwnerDTO {

    private UUID id;

    @NotNull
    @NotBlank
    @Size(max = 75)
    private String name;

    @NotBlank
    @Size(max = 250)
    private String address;

    @NotNull
    @NotBlank
    @Size(max = 20)
    private String telephone;

    private int version;

    private Set<PetDTO> pets;

}