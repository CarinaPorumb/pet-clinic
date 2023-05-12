package com.PetClinic.repository;

import com.PetClinic.entity.Pet;
import com.PetClinic.model.enums.PetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PetRepository extends JpaRepository<Pet, UUID> {

    Page<Pet> findAllByNameIsLikeIgnoreCase(String name, Pageable pageable);

    Page<Pet> findAllByPetType(PetType petType, Pageable pageable);

    Page<Pet> findAllByNameIsLikeIgnoreCaseAndPetType(String name, PetType petType, Pageable pageable);

    Page<Pet> findAllByAge(Integer age, Pageable pageable);

    Page<Pet> findAllByWeight(Double weight, Pageable pageable);

}