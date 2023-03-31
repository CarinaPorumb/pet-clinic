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

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class Pet implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false)
    private UUID id;

    @NotNull
    @NotBlank
    @Size(max = 75)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PetType petType;

//    @ManyToOne
//    @JoinColumn(name = "owner_id")
//    private Owner owner;

    private LocalDate birthDate;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pet")
//    @ToString.Exclude
//    @Builder.Default
//    private Set<Visit> visits = new HashSet<>();
}