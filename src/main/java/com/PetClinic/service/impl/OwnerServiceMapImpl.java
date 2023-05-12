package com.PetClinic.service.impl;

import com.PetClinic.model.OwnerDTO;
import com.PetClinic.service.OwnerService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class OwnerServiceMapImpl implements OwnerService {

    private Map<UUID, com.PetClinic.model.OwnerDTO> ownerDTOMap;

    public OwnerServiceMapImpl() {
        this.ownerDTOMap = new HashMap<>();
        OwnerDTO owner1 = OwnerDTO.builder()
                .id(UUID.randomUUID())
                .name("John")
                .address("Paris")
                .telephone("01234")
                .build();

        OwnerDTO owner2 = OwnerDTO.builder()
                .id(UUID.randomUUID())
                .name("Helen")
                .address("London")
                .telephone("123456")
                .build();

        OwnerDTO owner3 = OwnerDTO.builder()
                .id(UUID.randomUUID())
                .name("Maria")
                .address("Lisbon")
                .telephone("23456")
                .build();

        ownerDTOMap.put(owner1.getId(), owner1);
        ownerDTOMap.put(owner2.getId(), owner2);
        ownerDTOMap.put(owner3.getId(), owner3);
    }

    @Override
    public List<com.PetClinic.model.OwnerDTO> listOwners() {
        return new ArrayList<>(ownerDTOMap.values());
    }

    @Override
    public Optional<com.PetClinic.model.OwnerDTO> getById(UUID id) {
        return Optional.of(ownerDTOMap.get(id));
    }

    @Override
    public com.PetClinic.model.OwnerDTO saveNewOwner(com.PetClinic.model.OwnerDTO ownerDTO) {
        com.PetClinic.model.OwnerDTO savedOwnerDTO = com.PetClinic.model.OwnerDTO.builder()
                .id(UUID.randomUUID())
                .name(ownerDTO.getName())
                .address(ownerDTO.getAddress())
                .telephone(ownerDTO.getTelephone())
                .build();
        ownerDTOMap.put(savedOwnerDTO.getId(), savedOwnerDTO);
        return savedOwnerDTO;
    }

    @Override
    public Optional<com.PetClinic.model.OwnerDTO> updateOwner(UUID id, com.PetClinic.model.OwnerDTO ownerDTO) {
        com.PetClinic.model.OwnerDTO ownerDTOExisting = ownerDTOMap.get(id);
        ownerDTOExisting.setName(ownerDTO.getName());
        ownerDTOExisting.setAddress(ownerDTO.getAddress());
        ownerDTOExisting.setTelephone(ownerDTO.getTelephone());
        return Optional.of(ownerDTOExisting);
    }

    @Override
    public boolean deleteById(UUID id) {
        ownerDTOMap.remove(id);
        return true;
    }

    @Override
    public Optional<com.PetClinic.model.OwnerDTO> patchById(UUID id, com.PetClinic.model.OwnerDTO ownerDTO) {
        com.PetClinic.model.OwnerDTO existing = ownerDTOMap.get(id);

        if (StringUtils.hasText(ownerDTO.getName())) {
            existing.setName(ownerDTO.getName());
        }
        if (StringUtils.hasText(ownerDTO.getAddress())) {
            existing.setAddress(ownerDTO.getAddress());
        }
        if (StringUtils.hasText(ownerDTO.getTelephone())) {
            existing.setTelephone(ownerDTO.getTelephone());
        }
        return Optional.of(existing);
    }
}
