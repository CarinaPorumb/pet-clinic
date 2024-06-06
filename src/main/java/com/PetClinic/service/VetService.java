package com.PetClinic.service;

import com.PetClinic.model.VetDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface VetService {

    Page<VetDTO> listVets(String speciality, int pageNumber, int pageSize);

    Optional<VetDTO> getVetById(UUID id);

    VetDTO saveNewVet(VetDTO vetDTO);

    Optional<VetDTO> updateVet(UUID id, VetDTO vetDTO);

    boolean deleteVetById(UUID id);

    Optional<VetDTO> patchVetById(UUID id, VetDTO vetDTO);

}