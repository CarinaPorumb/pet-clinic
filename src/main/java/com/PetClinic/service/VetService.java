package com.PetClinic.service;

import com.PetClinic.model.VetDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VetService {

    Optional<VetDTO> getById(UUID id);

    List<VetDTO> listVets();

    VetDTO saveNewVet(VetDTO vetDTO);

    Optional<VetDTO> updateVet(UUID id, VetDTO vetDTO);

    boolean deleteById(UUID id);

    Optional<VetDTO> patchById(UUID id, VetDTO vetDTO);

}