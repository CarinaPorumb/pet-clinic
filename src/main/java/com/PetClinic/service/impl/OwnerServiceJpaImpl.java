package com.PetClinic.service.impl;

import com.PetClinic.mapper.OwnerMapper;
import com.PetClinic.model.OwnerDTO;
import com.PetClinic.repository.OwnerRepository;
import com.PetClinic.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Primary
@RequiredArgsConstructor
@Service
public class OwnerServiceJpaImpl implements OwnerService {

    private static final Logger log = LoggerFactory.getLogger(OwnerServiceJpaImpl.class);
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<OwnerDTO> listOwners(int pageNumber, int pageSize) {
        try {
            PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());
            Page<OwnerDTO> ownerDTOS = ownerRepository.findAll(pageRequest)
                    .map(ownerMapper::ownerToOwnerDto);
            log.info("Successfully retrieved {} owners", ownerDTOS.getSize());
            return ownerDTOS;
        } catch (Exception e) {
            log.error("Failed to retrieve owners", e);
            throw new RuntimeException("Failed to retrieve owners", e);
        }
    }

    @Override
    public Optional<OwnerDTO> getById(UUID id) {
        try {
            Optional<OwnerDTO> ownerDTO = Optional.ofNullable(ownerMapper.ownerToOwnerDto(ownerRepository.findById(id).orElse(null)));
            log.info("Successfully retrieved owner for ID: {}", id);
            return ownerDTO;
        } catch (Exception e) {
            log.error("Failed to retrieve owner for ID: {}", id, e);
            throw new RuntimeException("Failed to retrieve owner", e);
        }
    }

    @Override
    public OwnerDTO saveNewOwner(OwnerDTO ownerDTO) {
        try {
            OwnerDTO savedOwner = ownerMapper.ownerToOwnerDto(ownerRepository.save(ownerMapper.ownerDtoToOwner(ownerDTO)));
            log.info("Successfully saved new owner with ID: {}", savedOwner.getId());
            return savedOwner;
        } catch (Exception e) {
            log.error("Failed to save new owner", e);
            throw new RuntimeException("Failed to save new owner", e);

        }
    }

    @Override
    public Optional<OwnerDTO> updateOwner(UUID id, OwnerDTO ownerDTO) {
        try {
            AtomicReference<Optional<OwnerDTO>> atomicReference = new AtomicReference<>();
            ownerRepository.findById(id).ifPresentOrElse(foundOwner -> {
                foundOwner.setName(ownerDTO.getName());
                foundOwner.setAddress(ownerDTO.getAddress());
                foundOwner.setTelephone(ownerDTO.getTelephone());
                atomicReference.set(Optional.of(ownerMapper.ownerToOwnerDto(ownerRepository.save(foundOwner))));
                log.info("Updated owner with ID: {}", id);
            }, () -> {
                log.info("No owner found with ID: {}", id);
                atomicReference.set(Optional.empty());
            });
            return atomicReference.get();
        } catch (
                Exception e) {
            log.error("Failed to update owner with ID: {}", id, e);
            throw new RuntimeException("Failed to update owner", e);
        }
    }

    @Override
    public boolean deleteById(UUID id) {
        try {
            if (ownerRepository.existsById(id)) {
                ownerRepository.deleteById(id);
                log.info("Deleted owner with ID: {}", id);
                return true;
            }
            log.info("No owner found to delete with ID: {}", id);
            return false;
        } catch (Exception e) {
            log.error("Failed to delete owner with ID: {}", id, e);
            throw new RuntimeException("Failed to delete owner", e);
        }
    }

    @Override
    public Optional<OwnerDTO> patchById(UUID id, OwnerDTO ownerDTO) {
        try {
            AtomicReference<Optional<OwnerDTO>> atomicReference = new AtomicReference<>();
            ownerRepository.findById(id).ifPresentOrElse(foundOwner -> {
                boolean isUpdated = false;
                if (StringUtils.hasText(ownerDTO.getName()) && !ownerDTO.getName().equals(foundOwner.getName())) {
                    foundOwner.setName(ownerDTO.getName());
                    isUpdated = true;
                }
                if (ownerDTO.getTelephone() != null && !ownerDTO.getTelephone().equals(foundOwner.getTelephone())) {
                    foundOwner.setTelephone(ownerDTO.getTelephone());
                    isUpdated = true;
                }
                if (ownerDTO.getAddress() != null && !ownerDTO.getAddress().equals(foundOwner.getAddress())) {
                    foundOwner.setAddress(ownerDTO.getAddress());
                    isUpdated = true;
                }
                if (isUpdated) {
                    OwnerDTO updatedOwner = ownerMapper.ownerToOwnerDto(ownerRepository.save(foundOwner));
                    atomicReference.set(Optional.of(updatedOwner));
                    log.info("Owner with ID: {} patched successfully", id);
                } else {
                    atomicReference.set(Optional.of(ownerMapper.ownerToOwnerDto(foundOwner)));
                    log.info("No changes applied to owner with ID: {}", id);
                }
            }, () -> {
                log.info("No owner found with ID: {}", id);
                atomicReference.set(Optional.empty());
            });
            return atomicReference.get();
        } catch (Exception e) {
            log.error("Failed to patch owner with ID: {}", id, e);
            throw new RuntimeException("Failed to patch owner", e);
        }
    }

}