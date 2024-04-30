package com.PetClinic.service.impl;

import com.PetClinic.entity.Pet;
import com.PetClinic.exception.NotFoundException;
import com.PetClinic.mapper.PetMapper;
import com.PetClinic.model.PetDTO;
import com.PetClinic.model.enums.PetType;
import com.PetClinic.repository.PetRepository;
import com.PetClinic.service.PetService;
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
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
@Primary
@Service
public class PetServiceJpaImpl implements PetService {

    private static final Logger log = LoggerFactory.getLogger(PetServiceJpaImpl.class);
    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    @Override
    public Page<PetDTO> listPets(String name, PetType petType, Integer age, Double weight, Integer pageNumber, Integer pageSize) {
        log.info("Listing pets with filters - Name: {}, Type: {}, Age: {}, Weight: {}, Page: {}, Size: {}", name, petType, age, weight, pageNumber, pageSize);
        try {
            PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
            Page<Pet> petPage = determinePetPage(name, petType, pageRequest);
            return petPage.map(petMapper::petToPetDto);
        } catch (Exception e) {
            log.error("Error listing pets", e);
            throw new ServiceException("Failed to list pets", e);
        }
    }

    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Order.asc("name"));
        log.info("Building page request: page number = {}, page size = {}, sort = {}", queryPageNumber, queryPageSize, sort);
        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    private Page<Pet> determinePetPage(String name, PetType petType, PageRequest pageRequest) {
        if (StringUtils.hasText(name) && petType == null) {
            log.info("Querying pets by name: {}", name);
            return listPetsByName(name, pageRequest);
        } else if (!StringUtils.hasText(name) && petType != null) {
            log.info("Querying pets by type: {}", petType);
            return listPetsByType(petType, pageRequest);
        } else if (StringUtils.hasText(name) && petType != null) {
            log.info("Querying pets by name and type: {}, {}", name, petType);
            return listPetsByNameAndType(name, petType, pageRequest);
        } else {
            log.debug("Querying all pets with no specific filters");
            return petRepository.findAll(pageRequest);
        }
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
    public Optional<PetDTO> getById(UUID id) {
        try {
            return Optional.ofNullable(petRepository.findById(id)
                    .map(petMapper::petToPetDto)
                    .orElseThrow(() -> new NotFoundException("Pet", id)));
        } catch (Exception e) {
            log.error("Failed to retrieve pet for ID: {}", id, e);
            throw new ServiceException("An error occurred while retrieving pet", e);
        }
    }

    @Override
    public PetDTO saveNewPet(PetDTO petDTO) {
        try {
            PetDTO savedPet = petMapper.petToPetDto(petRepository.save(petMapper.petDtoToPet(petDTO)));
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
                atomicReference.set(Optional.of(petMapper.petToPetDto(petRepository.save(foundPet))));
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
    public boolean deleteById(UUID id) {
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
    public Optional<PetDTO> patchById(UUID id, PetDTO petDTO) {
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
                    PetDTO updatedPet = petMapper.petToPetDto(petRepository.save(foundPet));
                    atomicReference.set(Optional.of(updatedPet));
                    log.info("Pet with ID: {} patched successfully", id);
                } else {
                    atomicReference.set(Optional.of(petMapper.petToPetDto(foundPet)));
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
}