package com.PetClinic.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Builder
@Data
public class OwnerDTO {

    private UUID id;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 75, message = "Name must not exceed 75 characters")
    private String name;

    private String address;

    @NotBlank(message = "Telephone must not be blank")
    private String telephone;

    private Set<UUID> petIds;

}