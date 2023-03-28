package com.PetClinic.entity;

import com.PetClinic.model.enums.Speciality;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@ToString
@Entity
@NoArgsConstructor
public class Vet extends Person {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "vet_specialities",
            joinColumns = @JoinColumn(name = "vet_id"),
            inverseJoinColumns = @JoinColumn(name = "speciality_id"))
    private Speciality speciality;


    @Builder
    public Vet(UUID id, String name) {
        super(id, name);
    }

    public static class VetBuilder extends PersonBuilder {
        VetBuilder() {
            super();
        }
    }
}