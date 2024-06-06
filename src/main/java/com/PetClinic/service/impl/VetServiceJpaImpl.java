package com.PetClinic.service.impl;

import com.PetClinic.mapper.VetMapper;
import com.PetClinic.model.VetDTO;
import com.PetClinic.repository.VetRepository;
import com.PetClinic.service.VetService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
@Primary
@RequiredArgsConstructor
@Service
public class VetServiceJpaImpl implements VetService {

    private final VetRepository vetRepository;
    private final VetMapper vetMapper;

    @Override
    public Page<VetDTO> listVets(String speciality, int pageNumber, int pageSize) {
        return (Page<VetDTO>) vetRepository.findAll()
                .stream()
                .map(vetMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VetDTO> getVetById(UUID id) {
        return Optional.ofNullable(vetMapper.toDTO(vetRepository.findById(id).orElse(null)));
    }

    @Override
    public VetDTO saveNewVet(VetDTO vetDTO) {
        return vetMapper.toDTO(vetRepository.save(vetMapper.toEntity(vetDTO)));
    }

    @Override
    public Optional<VetDTO> updateVet(UUID id, VetDTO vetDTO) {
        AtomicReference<Optional<VetDTO>> atomicReference = new AtomicReference<>();
        vetRepository.findById(id).ifPresentOrElse(foundVet -> {
            foundVet.setName(vetDTO.getName());
            foundVet.setSpeciality(vetDTO.getSpeciality());
            atomicReference.set(Optional.of(vetMapper.toDTO(vetRepository.save(foundVet))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public boolean deleteVetById(UUID id) {
        if (vetRepository.existsById(id)) {
            vetRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<VetDTO> patchVetById(UUID id, VetDTO vetDTO) {
        AtomicReference<Optional<VetDTO>> atomicReference = new AtomicReference<>();
        vetRepository.findById(id).ifPresentOrElse(foundVet -> {
            if (StringUtils.hasText(vetDTO.getName())) {
                foundVet.setName(vetDTO.getName());
            }
            if (vetDTO.getSpeciality() != null) {
                foundVet.setSpeciality(vetDTO.getSpeciality());
            }
            atomicReference.set(Optional.of(vetMapper.toDTO(vetRepository.save(foundVet))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

}