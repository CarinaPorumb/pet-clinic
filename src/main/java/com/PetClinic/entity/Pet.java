package com.PetClinic.entity;

import com.PetClinic.model.enums.PetType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Pet {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "pet_id", length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @NotBlank
    @Size(max = 75)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PetType petType;

    private Integer age;

    private Double weight;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "visit_id")
    private Visit visit;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "pets_vets",
            joinColumns = @JoinColumn(name = "pet_id", referencedColumnName = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "vet_id", referencedColumnName = "vet_id"))
    private Set<Vet> vets = new HashSet<>();

    public void addVet(Vet vet) {
        this.vets.add(vet);
        vet.getPets().add(this);
    }

    public void removeVet(Vet vet) {
        this.vets.remove(vet);
        vet.getPets().remove(vet);
    }

    public Pet(UUID id, String name, PetType petType, Integer age, Double weight, Owner owner, Visit visit, Set<Vet> vets) {
        this.id = id;
        this.name = name;
        this.petType = petType;
        this.age = age;
        this.weight = weight;
        this.setOwner(owner);
        this.visit = visit;
        this.vets = vets;
    }

}