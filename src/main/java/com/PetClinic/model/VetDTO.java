package com.PetClinic.model;

import com.PetClinic.model.enums.Speciality;
import lombok.Data;

import java.util.UUID;


@Data
public class VetDTO {

    private UUID id;
    private String name;
    private Speciality speciality;

}