package com.PetClinic.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class OwnerDTO {

    private UUID id;
    private String name;
    private String address;
    private String telephone;
    private Set<PetDTO> petDTOSet = new HashSet<>();

}