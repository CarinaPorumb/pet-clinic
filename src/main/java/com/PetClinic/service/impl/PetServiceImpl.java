package com.PetClinic.service.impl;

import com.PetClinic.mapper.PetMapper;
import com.PetClinic.model.PetDTO;
import com.PetClinic.repository.PetRepository;
import com.PetClinic.service.PetService;
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
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;

    @Override
    public Optional<PetDTO> getById(UUID id) {
        return Optional.ofNullable(petMapper.petToPetDto(petRepository.findById(id).orElse(null)));
    }

    @Override
    public List<PetDTO> listPets() {
        return petRepository.findAll()
                .stream()
                .map(petMapper::petToPetDto)
                .collect(Collectors.toList());
    }

    @Override
    public PetDTO saveNewPet(PetDTO petDTO) {
        return petMapper.petToPetDto(petRepository.save(petMapper.petDtoToPet(petDTO)));
    }

    @Override
    public Optional<PetDTO> updatePet(UUID id, PetDTO petDTO) {
        AtomicReference<Optional<PetDTO>> atomicReference = new AtomicReference<>();
        petRepository.findById(id).ifPresentOrElse(foundPet -> {
            foundPet.setName(petDTO.getName());
            foundPet.setPetType(petDTO.getPetType());
            foundPet.setBirthDate(petDTO.getBirthDate());
            atomicReference.set(Optional.of(petMapper.petToPetDto(petRepository.save(foundPet))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();
    }

    @Override
    public boolean deleteById(UUID id) {
        if (petRepository.existsById(id)) {
            petRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<PetDTO> patchById(UUID id, PetDTO petDTO) {
        AtomicReference<Optional<PetDTO>> atomicReference = new AtomicReference<>();
        petRepository.findById(id).ifPresentOrElse(foundPet -> {
            if (StringUtils.hasText(petDTO.getName())){
                foundPet.setName(petDTO.getName());
            }
            if (petDTO.getPetType() != null) {
                foundPet.setPetType(petDTO.getPetType());
            }
            if (petDTO.getBirthDate() != null) {
                foundPet.setBirthDate(petDTO.getBirthDate());
            }
            atomicReference.set(Optional.of(petMapper.petToPetDto(petRepository.save(foundPet))));
            }, () -> {
                atomicReference.set(Optional.empty());
            });
        return atomicReference.get();
    }
}