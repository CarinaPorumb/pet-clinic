package com.PetClinic.service.impl;

import com.PetClinic.mapper.OwnerMapper;
import com.PetClinic.model.OwnerDTO;
import com.PetClinic.repository.OwnerRepository;
import com.PetClinic.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
@Primary
@RequiredArgsConstructor
@Service
public class OwnerServiceJpaImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    @Override
    public List<OwnerDTO> listOwners() {
        return ownerRepository.findAll()
                .stream()
                .map(ownerMapper::ownerToOwnerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OwnerDTO> getById(UUID id) {
        return Optional.ofNullable(ownerMapper.ownerToOwnerDto(ownerRepository.findById(id).orElse(null)));
    }

    @Override
    public OwnerDTO saveNewOwner(OwnerDTO ownerDTO) {
        return ownerMapper.ownerToOwnerDto(ownerRepository.save(ownerMapper.ownerDtoToOwner(ownerDTO)));
    }

    @Override
    public Optional<OwnerDTO> updateOwner(UUID id, OwnerDTO ownerDTO) {
        AtomicReference<Optional<OwnerDTO>> atomicReference = new AtomicReference<>();
        ownerRepository.findById(id).ifPresentOrElse(foundOwner -> {
            foundOwner.setName(ownerDTO.getName());
            foundOwner.setAddress(ownerDTO.getAddress());
            foundOwner.setTelephone(ownerDTO.getTelephone());
            atomicReference.set(Optional.of(ownerMapper.ownerToOwnerDto(ownerRepository.save(foundOwner))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public boolean deleteById(UUID id) {
        if (ownerRepository.existsById(id)) {
            ownerRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<OwnerDTO> patchById(UUID id, OwnerDTO ownerDTO) {
        AtomicReference<Optional<OwnerDTO>> atomicReference = new AtomicReference<>();
        ownerRepository.findById(id).ifPresentOrElse(foundOwner -> {
            if (StringUtils.hasText(ownerDTO.getName())) {
                foundOwner.setName(ownerDTO.getName());
            }
            if (ownerDTO.getTelephone() != null) {
                foundOwner.setTelephone(ownerDTO.getTelephone());
            }
            if (ownerDTO.getAddress() != null) {
                foundOwner.setAddress(ownerDTO.getAddress());
            }
            atomicReference.set(Optional.of(ownerMapper.ownerToOwnerDto(ownerRepository.save(foundOwner))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

}