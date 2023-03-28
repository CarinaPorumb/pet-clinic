package com.PetClinic.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;


@EqualsAndHashCode(callSuper = true)
@Data
public class OwnerDTO extends PersonDTO {
    private String address;
    private String telephone;
    private Set<PetDTO> petDTOSet = new HashSet<>();


}