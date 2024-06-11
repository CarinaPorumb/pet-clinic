package com.PetClinic.service.impl;

import com.PetClinic.entity.Vet;
import com.PetClinic.mapper.VetMapper;
import com.PetClinic.model.VetDTO;
import com.PetClinic.repository.VetRepository;
import com.PetClinic.service.VetService;
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
public class VetServiceJpaImpl implements VetService {

    private final VetRepository vetRepository;
    private final VetMapper vetMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<VetDTO> listVets(String name, String speciality, Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());

        Specification<Vet> vetSpecification = Specification
                .where(SpecificationUtils.<Vet>attributeLike("name", name))
                .and(SpecificationUtils.attributeLike("speciality", speciality));

        log.info("Listing vet with filters - Name: {}, Speciality: {}", name, speciality);
        return vetRepository.findAll(vetSpecification, pageable).map(vetMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VetDTO> getVetById(UUID id) {
        log.debug("Attempting to find vet with ID: {}", id);
        return vetRepository.findById(id)
                .map(vetMapper::toDTO);
    }

    @Override
    @Transactional
    public VetDTO saveNewVet(VetDTO vetDTO) {
        log.debug("Saving new vet: {}", vetDTO);
        Vet savedVet = vetRepository.save(vetMapper.toEntity(vetDTO));
        log.info("Successfully saved new vet with ID: {}", savedVet.getId());
        return vetMapper.toDTO(savedVet);
    }

    @Override
    @Transactional
    public Optional<VetDTO> updateVet(UUID id, VetDTO vetDTO) {
        log.debug("Updating vet with ID: {}", id);
        return vetRepository.findById(id)
                .map(existingVet -> {
                    existingVet.setName(vetDTO.getName());
                    existingVet.setSpeciality(vetDTO.getSpeciality());
                    vetRepository.save(existingVet);
                    log.info("Vet updated with ID: {}", existingVet.getId());
                    return vetMapper.toDTO(existingVet);
                });
    }

    @Override
    @Transactional
    public boolean deleteVetById(UUID id) {
        log.debug("Attempting to delete vet with ID: {}", id);
        return vetRepository.findById(id)
                .map(vet -> {
                    vetRepository.delete(vet);
                    log.info("Vet deleted with ID: {}", id);
                    return true;
                }).orElse(false);

    }

    @Override
    @Transactional
    public Optional<VetDTO> patchVetById(UUID id, VetDTO vetDTO) {
        log.debug("Patching vet with ID: {}", id);

        return vetRepository.findById(id).map(existingVet -> {
            boolean isUpdated = false;

            if (StringUtils.hasText(vetDTO.getName()) && !existingVet.getName().equals(vetDTO.getName())) {
                existingVet.setName(vetDTO.getName());
                isUpdated = true;
            }
            if (vetDTO.getSpeciality() != null && !existingVet.getSpeciality().equals(vetDTO.getSpeciality())) {
                existingVet.setSpeciality(vetDTO.getSpeciality());
                isUpdated = true;
            }

            if (isUpdated) {
                vetRepository.save(existingVet);
                log.info("Vet with ID: {} patched successfully", id);
            } else {
                log.info("No changes applied to vet with ID: {}", id);
            }
            return vetMapper.toDTO(existingVet);
        });
    }

}