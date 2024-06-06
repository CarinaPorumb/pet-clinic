package com.PetClinic.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class OwnerDTO {

    private UUID id;

    @NotBlank(message = "Name must not be blank")
    private String name;

    private String address;

    @NotBlank(message = "Telephone must not be blank")
    private String telephone;

    private Set<UUID> petIds;

}