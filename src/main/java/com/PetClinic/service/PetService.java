package com.PetClinic.service;

import com.PetClinic.model.PetDTO;
import com.PetClinic.model.enums.PetType;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface PetService {

    Page<PetDTO> listPets(String name, PetType petType, Integer age, Double weight, Integer pageNumber, Integer pageSize);

    Optional<PetDTO> getById(UUID id);

    PetDTO saveNewPet(PetDTO petDTO);

    Optional<PetDTO> updatePet(UUID id, PetDTO petDTO);

    boolean deleteById(UUID id);

    Optional<PetDTO> patchById(UUID id, PetDTO petDTO);

}