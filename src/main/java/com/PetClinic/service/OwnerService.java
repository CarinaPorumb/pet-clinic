package com.PetClinic.service;

import com.PetClinic.model.OwnerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OwnerService {

    List<OwnerDTO> listOwners();

    Optional<OwnerDTO> getById(UUID id);

    OwnerDTO saveNewOwner(OwnerDTO ownerDTO);

    Optional<OwnerDTO> updateOwner(UUID id, OwnerDTO ownerDTO);

    boolean deleteById(UUID id);

    Optional<OwnerDTO> patchById(UUID id, OwnerDTO ownerDTO);

}