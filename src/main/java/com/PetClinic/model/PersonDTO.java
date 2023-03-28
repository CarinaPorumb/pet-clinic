package com.PetClinic.model;

import lombok.Data;

import java.util.UUID;

@Data
public class PersonDTO {
    private UUID id;
    private String name;

    public PersonDTO(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public PersonDTO() {

    }
}