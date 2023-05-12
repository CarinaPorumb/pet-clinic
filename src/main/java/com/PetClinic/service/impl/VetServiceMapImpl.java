package com.PetClinic.service.impl;

import com.PetClinic.model.VetDTO;
import com.PetClinic.service.VetService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
@Service
public class VetServiceMapImpl implements VetService {

    private Map<UUID, VetDTO> vetDTOMap;

    @Override
    public List<VetDTO> listVets() {
        return new ArrayList<>(vetDTOMap.values());
    }

    @Override
    public Optional<VetDTO> getById(UUID id) {
        return Optional.of(vetDTOMap.get(id));
    }

    @Override
    public VetDTO saveNewVet(VetDTO vetDTO) {
        VetDTO savedVetDTO = VetDTO.builder()
                .id(UUID.randomUUID())
                .name(vetDTO.getName())
                .speciality(vetDTO.getSpeciality())
                .build();
        vetDTOMap.put(savedVetDTO.getId(), savedVetDTO);
        return savedVetDTO;
    }

    @Override
    public Optional<VetDTO> updateVet(UUID id, VetDTO vetDTO) {
        VetDTO existing = vetDTOMap.get(id);
        existing.setName(vetDTO.getName());
        existing.setSpeciality(vetDTO.getSpeciality());
        return Optional.of(existing);
    }

    @Override
    public boolean deleteById(UUID id) {
        vetDTOMap.remove(id);
        return true;
    }

    @Override
    public Optional<VetDTO> patchById(UUID id, VetDTO vetDTO) {
        VetDTO existing = vetDTOMap.get(id);
        if (StringUtils.hasText(vetDTO.getName()))
            existing.setName(vetDTO.getName());
        if (vetDTO.getSpeciality() != null)
            existing.setSpeciality(vetDTO.getSpeciality());
        return Optional.of(existing);
    }
}
