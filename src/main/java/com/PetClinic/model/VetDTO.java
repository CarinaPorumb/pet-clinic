package com.PetClinic.model;

import com.PetClinic.model.enums.Speciality;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)

@Data
public class VetDTO extends PersonDTO {
    private Speciality speciality;

}