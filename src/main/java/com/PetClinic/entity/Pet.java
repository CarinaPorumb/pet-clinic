package com.PetClinic.entity;

import com.PetClinic.model.enums.PetType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Pet extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "pet_id", updatable = false, nullable = false)
    private UUID id;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 75)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PetType petType;

    @PositiveOrZero(message = "Age must be non-negative")
    private Integer age;

    @PositiveOrZero(message = "Weight must be non-negative")
    private Double weight;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @ToString.Exclude
    private Owner owner;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<Visit> visits = new HashSet<>();

    @Builder.Default
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
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
        vet.getPets().remove(this);
    }

    @Version
    private int version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pet pet = (Pet) o;
        return Objects.equals(id, pet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}