package com.PetClinic.service.impl;

import com.PetClinic.entity.Owner;
import com.PetClinic.mapper.OwnerMapper;
import com.PetClinic.model.OwnerDTO;
import com.PetClinic.repository.OwnerRepository;
import com.PetClinic.repository.PetRepository;
import com.PetClinic.service.OwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Primary
@RequiredArgsConstructor
@Service
public class OwnerServiceJpaImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;
    private final PetRepository petRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<OwnerDTO> listOwners(String name, String address, String telephone, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());

        Specification<Owner> ownerSpecification = Specification.where(SpecificationUtils.<Owner>attributeLike("name", name))
                .and(SpecificationUtils.attributeLike("address", address))
                .and(SpecificationUtils.attributeLike("telephone", telephone));

        log.debug("Listing owners with filters - Name: {}, Address {}, Telephone {},", name, address, telephone);
        return ownerRepository.findAll(ownerSpecification, pageable).map(ownerMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OwnerDTO> getOwnerById(UUID id) {
        log.debug("Attempting to find owner with ID: {}", id);
        return ownerRepository.findById(id)
                .map(ownerMapper::toDTO);
    }

    @Override
    @Transactional
    public OwnerDTO saveNewOwner(OwnerDTO ownerDTO) {
        log.debug("Saving new owner: {}", ownerDTO);
        Owner savedOwner = ownerRepository.save(ownerMapper.toEntity(ownerDTO));
        log.info("Successfully saved new pet with id: {}", savedOwner.getId());
        return ownerMapper.toDTO(savedOwner);
    }

    @Override
    @Transactional
    public Optional<OwnerDTO> updateOwner(UUID id, OwnerDTO ownerDTO) {
        log.debug("Updating owner with ID: {}", id);
        return ownerRepository.findById(id)
                .map(existingOwner -> {
                    existingOwner.setName(ownerDTO.getName());
                    existingOwner.setAddress(ownerDTO.getAddress());
                    existingOwner.setTelephone(ownerDTO.getTelephone());
                    ownerRepository.save(existingOwner);
                    log.info("Owner updated with ID: {}", existingOwner.getId());
                    return ownerMapper.toDTO(existingOwner);
                });
    }

    @Override
    @Transactional
    public boolean deleteOwnerById(UUID id) {
        log.debug("Attempting to delete owner with ID: {}", id);
        return ownerRepository.findById(id)
                .map(owner -> {
                    petRepository.deleteOwnerFromPet(id);
                    ownerRepository.delete(owner);
                    log.info("Deleted owner with ID: {}", id);
                    return true;
                }).orElse(false);
    }

    @Override
    @Transactional
    public Optional<OwnerDTO> patchOwnerById(UUID id, OwnerDTO ownerDTO) {
        log.debug("Patching owner with ID: {}", id);
        return ownerRepository.findById(id).map(existingOwner -> {
            boolean isUpdated = false;

            if (StringUtils.hasText(ownerDTO.getName()) && !existingOwner.getName().equals(ownerDTO.getName())) {
                existingOwner.setName(ownerDTO.getName());
                isUpdated = true;
            }
            if (StringUtils.hasText(ownerDTO.getAddress()) && !existingOwner.getAddress().equals(ownerDTO.getAddress())) {
                existingOwner.setAddress(ownerDTO.getAddress());
                isUpdated = true;
            }
            if (StringUtils.hasText(ownerDTO.getTelephone()) && !existingOwner.getTelephone().equals(ownerDTO.getTelephone())) {
                existingOwner.setTelephone(ownerDTO.getTelephone());
                isUpdated = true;
            }

            if (isUpdated) {
                ownerRepository.save(existingOwner);
                log.info("Owner with ID: {} patched successfully", id);
            } else {
                log.info("No changes applied to owner with ID: {}", id);
            }
            return ownerMapper.toDTO(existingOwner);
        });
    }
}