package com.PetClinic.service;

import com.PetClinic.model.PetDTO;
import com.PetClinic.model.enums.PetType;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface PetService {

    Page<PetDTO> listPets(String name, PetType petType, Integer age, Double weight, Integer pageNumber, Integer pageSize);

    Optional<PetDTO> getPetById(UUID id);

    PetDTO saveNewPet(PetDTO petDTO);

    Optional<PetDTO> updatePet(UUID id, PetDTO petDTO);

    boolean deletePetById(UUID id);

    Optional<PetDTO> patchPetById(UUID id, PetDTO petDTO);

}