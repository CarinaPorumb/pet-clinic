package com.PetClinic.service.impl;

import com.PetClinic.entity.Pet;
import com.PetClinic.exception.NotFoundException;
import com.PetClinic.mapper.PetMapper;
import com.PetClinic.model.PetCSVRecord;
import com.PetClinic.model.PetDTO;
import com.PetClinic.model.enums.PetType;
import com.PetClinic.repository.PetRepository;
import com.PetClinic.service.CSVConversionService;
import com.PetClinic.service.PetService;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Primary
@Service
public class PetServiceJpaImpl implements PetService, CSVConversionService<PetCSVRecord> {

    private static final Logger log = LoggerFactory.getLogger(PetServiceJpaImpl.class);
    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    @Override
    @Transactional(readOnly = true)
    public Page<PetDTO> listPets(String name, PetType petType, Integer age, Double weight, Integer pageNumber, Integer pageSize) {
        log.info("Listing pets with filters - Name: {}, Type: {}, Age: {}, Weight: {}, Page: {}, Size: {}", name, petType, age, weight, pageNumber, pageSize);
        try {
            PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
            Page<Pet> petPage = determinePetPage(name, petType, age, weight, pageRequest);
            return petPage.map(pet -> petMapper.toDTO(initializePet(pet)));
        } catch (Exception e) {
            log.error("Error listing pets", e);
            throw new ServiceException("Failed to list pets", e);
        }
    }


    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int page = (pageNumber == null || pageNumber < 0) ? 0 : pageNumber;
        int size = (pageSize == null || pageSize <= 0) ? 10 : pageSize;
        return PageRequest.of(page, size, Sort.by("name").ascending());
    }

    private Page<Pet> determinePetPage(String name, PetType petType, Integer age, Double weight, PageRequest pageRequest) {
        if (name != null && petType != null) {
            return petRepository.findAllByNameIsLikeIgnoreCaseAndPetType(name, petType, pageRequest);
        } else if (name != null) {
            return petRepository.findAllByNameIsLikeIgnoreCase(name, pageRequest);
        } else if (petType != null) {
            return petRepository.findAllByPetType(petType, pageRequest);
        } else if (age != null) {
            return petRepository.findAllByAge(age, pageRequest);
        } else if (weight != null) {
            return petRepository.findAllByWeight(weight, pageRequest);
        } else {
            return petRepository.findAll(pageRequest);
        }
    }


    private Pet initializePet(Pet pet) {
        pet.getVisits().size(); // Force initialization of visits collection
        return pet;
    }


    public Page<Pet> listPetsByName(String name, Pageable pageable) {
        log.debug("Fetching pets with name like '%{}%'", name);
        return petRepository.findAllByNameIsLikeIgnoreCase("%" + name + "%", pageable);
    }

    public Page<Pet> listPetsByType(PetType petType, Pageable pageable) {
        log.debug("Fetching pets by type: {}", petType);
        return petRepository.findAllByPetType(petType, pageable);
    }

    public Page<Pet> listPetsByNameAndType(String name, PetType petType, Pageable pageable) {
        log.debug("Fetching pets by name and type: {}, {}", name, petType);
        return petRepository.findAllByNameIsLikeIgnoreCaseAndPetType("%" + name + "%", petType, pageable);
    }

    @Override
    public Optional<PetDTO> getPetById(UUID id) {
        try {
            return Optional.ofNullable(petRepository.findById(id)
                    .map(petMapper::toDTO)
                    .orElseThrow(() -> new NotFoundException("Pet", id)));
        } catch (Exception e) {
            log.error("Failed to retrieve pet for ID: {}", id, e);
            throw new ServiceException("An error occurred while retrieving pet", e);
        }
    }

    @Override
    public PetDTO saveNewPet(PetDTO petDTO) {
        try {
            PetDTO savedPet = petMapper.toDTO(petRepository.save(petMapper.toEntity(petDTO)));
            log.info("Successfully saved new pet with ID: {}", savedPet.getId());
            return savedPet;
        } catch (Exception e) {
            log.error("Failed to save new pet", e);
            throw new RuntimeException("Failed to save new pet", e);
        }

    }

    @Override
    public Optional<PetDTO> updatePet(UUID id, PetDTO petDTO) {
        try {
            AtomicReference<Optional<PetDTO>> atomicReference = new AtomicReference<>();
            petRepository.findById(id).ifPresentOrElse(foundPet -> {
                foundPet.setName(petDTO.getName());
                foundPet.setPetType(petDTO.getPetType());
                foundPet.setAge(petDTO.getAge());
                foundPet.setWeight(petDTO.getWeight());
                atomicReference.set(Optional.of(petMapper.toDTO(petRepository.save(foundPet))));
                log.info("Updated pet with id {}", id);
            }, () -> {
                log.info("No pet found with ID: {}", id);
                atomicReference.set(Optional.empty());
            });
            return atomicReference.get();
        } catch (Exception e) {
            log.error("Failed to update pet with ID: {}", id, e);
            throw new ServiceException("Failed to update pet", e);
        }
    }

    @Override
    public boolean deletePetById(UUID id) {
        try {
            if (petRepository.existsById(id)) {
                petRepository.deleteById(id);
                log.info("Deleted pet with id {}", id);
                return true;
            }
            log.info("Pet with id {} not found", id);
            return false;
        } catch (Exception e) {
            log.error("Error deleting pet with id {}", id, e);
            throw new RuntimeException("Failed to delete pet with id " + id, e);
        }
    }

    @Override
    public Optional<PetDTO> patchPetById(UUID id, PetDTO petDTO) {
        try {
            AtomicReference<Optional<PetDTO>> atomicReference = new AtomicReference<>();
            petRepository.findById(id).ifPresentOrElse(foundPet -> {
                boolean isUpdated = false;
                if (StringUtils.hasText(petDTO.getName()) && !foundPet.getName().equals(petDTO.getName())) {
                    foundPet.setName(petDTO.getName());
                    isUpdated = true;
                }
                if (petDTO.getPetType() != null && !foundPet.getPetType().equals(petDTO.getPetType())) {
                    foundPet.setPetType(petDTO.getPetType());
                    isUpdated = true;
                }
                if (petDTO.getAge() != null && !foundPet.getAge().equals(petDTO.getAge())) {
                    foundPet.setAge(petDTO.getAge());
                    isUpdated = true;
                }
                if (petDTO.getWeight() != null && !foundPet.getWeight().equals(petDTO.getWeight())) {
                    foundPet.setWeight(petDTO.getWeight());
                    isUpdated = true;
                }
                if (isUpdated) {
                    PetDTO updatedPet = petMapper.toDTO(petRepository.save(foundPet));
                    atomicReference.set(Optional.of(updatedPet));
                    log.info("Pet with ID: {} patched successfully", id);
                } else {
                    atomicReference.set(Optional.of(petMapper.toDTO(foundPet)));
                    log.info("No changes applied to pet with ID: {}", id);
                }
            }, () -> {
                log.info("No pet found with ID: {}", id);
                atomicReference.set(Optional.empty());
            });
            return atomicReference.get();
        } catch (Exception e) {
            log.error("Failed to patch pet with ID: {}", id, e);
            throw new RuntimeException("Failed to patch pet with ID: ", e);
        }
    }

    @Override
    public List<PetCSVRecord> convertCSV(File csvFile) {
        try {
            return new CsvToBeanBuilder<PetCSVRecord>(new FileReader(csvFile))
                    .withType(PetCSVRecord.class)
                    .build().parse();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}