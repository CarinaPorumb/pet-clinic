package com.PetClinic.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Entity
public class Owner extends Person {
    private String address;
    private String telephone;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    @ToString.Exclude
    private Set<Pet> pets = new HashSet<>();

    public Owner() {
    }

    @Builder
    public Owner(UUID id, String name) {
        super(id, name);
    }

    public static class OwnerBuilder extends PersonBuilder {
        OwnerBuilder() {
            super();
        }
    }

}