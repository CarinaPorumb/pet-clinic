package com.PetClinic.service;

import com.PetClinic.model.PetDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PetService {

    Optional<PetDTO> getById(UUID id);

    List<PetDTO> listPets();

    PetDTO saveNewPet(PetDTO petDTO);

    Optional<PetDTO> updatePet(UUID id, PetDTO petDTO);

    boolean deleteById(UUID id);

    Optional<PetDTO> patchById(UUID id, PetDTO petDTO);

}