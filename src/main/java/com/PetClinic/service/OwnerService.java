package com.PetClinic.service;

import com.PetClinic.model.OwnerDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface OwnerService {

    Page<OwnerDTO> listOwners(int pageNumber, int pageSize);

    Optional<OwnerDTO> getById(UUID id);

    OwnerDTO saveNewOwner(OwnerDTO ownerDTO);

    Optional<OwnerDTO> updateOwner(UUID id, OwnerDTO ownerDTO);

    boolean deleteById(UUID id);

    Optional<OwnerDTO> patchById(UUID id, OwnerDTO ownerDTO);

}