package com.PetClinic.service.impl;

import com.PetClinic.model.PetDTO;
import com.PetClinic.model.enums.PetType;
import com.PetClinic.service.PetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class PetServiceMapImpl implements PetService {

    private Map<UUID, PetDTO> petDTOMap;

    @Override
    public Page<PetDTO> listPets(String name, PetType petType, Integer age, Double weight, Integer pageNumber, Integer pageSize) {
        return new PageImpl<>(new ArrayList<>(petDTOMap.values()));
    }

    @Override
    public Optional<PetDTO> getById(UUID id) {
        return Optional.of(petDTOMap.get(id));
    }

    @Override
    public PetDTO saveNewPet(PetDTO petDTO) {
        PetDTO savedPetDTO = PetDTO.builder()
                .id(UUID.randomUUID())
                .name(petDTO.getName())
                .petType(petDTO.getPetType())
                .age(petDTO.getAge())
                .weight(petDTO.getWeight())
                .build();
        petDTOMap.put(savedPetDTO.getId(), savedPetDTO);
        return savedPetDTO;
    }

    @Override
    public Optional<PetDTO> updatePet(UUID id, PetDTO petDTO) {
        PetDTO petDTOExisting = petDTOMap.get(id);
        petDTOExisting.setName(petDTO.getName());
        petDTOExisting.setPetType(petDTO.getPetType());
        petDTOExisting.setAge(petDTO.getAge());
        petDTOExisting.setWeight(petDTO.getWeight());
        return Optional.of(petDTOExisting);
    }

    @Override
    public boolean deleteById(UUID id) {
        petDTOMap.remove(id);
        return true;
    }

    @Override
    public Optional<PetDTO> patchById(UUID id, PetDTO petDTO) {
        PetDTO petDTOExisting = petDTOMap.get(id);

        if (StringUtils.hasText(petDTO.getName())) {
            petDTOExisting.setName(petDTO.getName());
        }
        if (petDTO.getPetType() != null) {
            petDTOExisting.setPetType(petDTO.getPetType());
        }
        if (petDTO.getAge() != null) {
            petDTOExisting.setAge(petDTO.getAge());
        }
        if (petDTO.getWeight() != null) {
            petDTOExisting.setWeight(petDTO.getWeight());
        }
        return Optional.of(petDTOExisting);
    }
}
