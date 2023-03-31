package com.PetClinic.service.impl;

import com.PetClinic.mapper.VetMapper;
import com.PetClinic.model.VetDTO;
import com.PetClinic.repository.VetRepository;
import com.PetClinic.service.VetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VetServiceImpl implements VetService {

    private final VetRepository vetRepository;
    private final VetMapper vetMapper;

    @Override
    public Optional<VetDTO> getById(UUID id) {
        return Optional.ofNullable(vetMapper.vetToVetDto(vetRepository.findById(id).orElse(null)));
    }

    @Override
    public List<VetDTO> listVets() {
        return vetRepository.findAll()
                .stream()
                .map(vetMapper::vetToVetDto)
                .collect(Collectors.toList());
    }

    @Override
    public VetDTO saveNewVet(VetDTO vetDTO) {
        return vetMapper.vetToVetDto(vetRepository.save(vetMapper.vetDtoToVet(vetDTO)));
    }

    @Override
    public Optional<VetDTO> updateVet(UUID id, VetDTO vetDTO) {
        AtomicReference<Optional<VetDTO>> atomicReference = new AtomicReference<>();
        vetRepository.findById(id).ifPresentOrElse(foundVet -> {
            foundVet.setName(vetDTO.getName());
            foundVet.setSpeciality(vetDTO.getSpeciality());
            atomicReference.set(Optional.of(vetMapper.vetToVetDto(vetRepository.save(foundVet))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public boolean deleteById(UUID id) {
        if (vetRepository.existsById(id)) {
            vetRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<VetDTO> patchById(UUID id, VetDTO vetDTO) {
        AtomicReference<Optional<VetDTO>> atomicReference = new AtomicReference<>();
        vetRepository.findById(id).ifPresentOrElse(foundVet -> {
            if (StringUtils.hasText(vetDTO.getName())) {
                foundVet.setName(vetDTO.getName());
            }
            if (vetDTO.getSpeciality() != null) {
                foundVet.setSpeciality(vetDTO.getSpeciality());
            }
            atomicReference.set(Optional.of(vetMapper.vetToVetDto(vetRepository.save(foundVet))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

}