package com.PetClinic.service.impl;

import com.PetClinic.entity.Pet;
import com.PetClinic.mapper.PetMapper;
import com.PetClinic.model.PetCSVRecord;
import com.PetClinic.model.PetDTO;
import com.PetClinic.model.enums.PetType;
import com.PetClinic.repository.PetRepository;
import com.PetClinic.service.CSVConversionService;
import com.PetClinic.service.PetService;
import com.opencsv.bean.CsvToBeanBuilder;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Primary
@Service
public class PetServiceJpaImpl implements PetService, CSVConversionService<PetCSVRecord> {

    private final PetRepository petRepository;
    private final PetMapper petMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<PetDTO> listPets(String name, PetType petType, Integer age, Double weight, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());

        Specification<Pet> petSpecification = Specification.where(SpecificationUtils.<Pet>attributeLike("name", name))
                .and(SpecificationUtils.attributeEquals("petType", petType))
                .and(SpecificationUtils.attributeEquals("age", age))
                .and(SpecificationUtils.attributeEquals("weight", weight));

        log.info("Listing pets with filters - Name: {}, Type: {}, Age: {}, Weight: {}", name, petType, age, weight);

        return petRepository.findAll(petSpecification, pageable).map(petMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PetDTO> getPetById(UUID id) {
        log.debug("Attempting to find owner with ID: {}", id);
        return petRepository.findById(id)
                .map(petMapper::toDTO);
    }

    @Override
    @Transactional
    public PetDTO saveNewPet(PetDTO petDTO) {
        log.debug("Saving new pet: {}", petDTO);
        Pet savedPet = petRepository.save(petMapper.toEntity(petDTO));
        log.info("Successfully saved new pet with ID: {}", savedPet.getId());
        return petMapper.toDTO(savedPet);
    }

    @Override
    @Transactional
    public Optional<PetDTO> updatePet(UUID id, PetDTO petDTO) {
        log.debug("Updating pet with ID: {}", id);
        return petRepository.findById(id)
                .map(existingPet -> {
                    existingPet.setName(petDTO.getName());
                    existingPet.setPetType(petDTO.getPetType());
                    existingPet.setAge(petDTO.getAge());
                    existingPet.setWeight(petDTO.getWeight());
                    petRepository.save(existingPet);
                    log.info("Pet updated with ID: {}", existingPet.getId());
                    return petMapper.toDTO(existingPet);
                });
    }

    @Override
    @Transactional
    public boolean deletePetById(UUID id) {
        log.debug("Attempting to delete pet with ID: {}", id);
        return petRepository.findById(id)
                .map(pet -> {
                    petRepository.delete(pet);
                    log.info("Deleted owner with ID: {}", id);
                    return true;
                }).orElse(false);
    }

    @Override
    @Transactional
    public Optional<PetDTO> patchPetById(UUID id, PetDTO petDTO) {
        log.debug("Patching pet with id {}", id);
        return petRepository.findById(id).map(existingPet -> {
            boolean isUpdated = false;

            if (StringUtils.hasText(petDTO.getName()) && !existingPet.getName().equals(petDTO.getName())) {
                existingPet.setName(petDTO.getName());
                isUpdated = true;
            }
            if (petDTO.getPetType() != null && petDTO.getPetType() != existingPet.getPetType()) {
                existingPet.setPetType(petDTO.getPetType());
                isUpdated = true;
            }
            if (petDTO.getAge() != null && !petDTO.getAge().equals(existingPet.getAge())) {
                existingPet.setAge(petDTO.getAge());
                isUpdated = true;
            }
            if (petDTO.getWeight() != null && !petDTO.getWeight().equals(existingPet.getWeight())) {
                existingPet.setWeight(petDTO.getWeight());
                isUpdated = true;
            }
            if (isUpdated) {
                petRepository.save(existingPet);
                log.info("Pet with ID: {} patched successfully", id);
            } else {
                log.info("No changes applied to pet with ID: {}", id);
            }
            return petMapper.toDTO(existingPet);
        });
    }

    @Override
    public List<PetCSVRecord> convertCSV(File csvFile) {
        try {
            return new CsvToBeanBuilder<PetCSVRecord>(new FileReader(csvFile))
                    .withType(PetCSVRecord.class)
                    .build().parse();
        } catch (FileNotFoundException e) {
            log.error("CSv file not found: {}", csvFile.getPath(), e);
            throw new RuntimeException("CSv file not found", e);
        }
    }
}