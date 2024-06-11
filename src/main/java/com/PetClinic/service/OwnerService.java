package com.PetClinic.service;

import com.PetClinic.model.OwnerDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface OwnerService {

    Page<OwnerDTO> listOwners(String name, String address, String telephone, Integer pageNumber, Integer pageSize);

    Optional<OwnerDTO> getOwnerById(UUID id);

    OwnerDTO saveNewOwner(OwnerDTO ownerDTO);

    Optional<OwnerDTO> updateOwner(UUID id, OwnerDTO ownerDTO);

    boolean deleteOwnerById(UUID id);

    Optional<OwnerDTO> patchOwnerById(UUID id, OwnerDTO ownerDTO);

}