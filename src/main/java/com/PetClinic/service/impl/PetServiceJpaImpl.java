package com.PetClinic.service.impl;

import com.PetClinic.entity.Pet;
import com.PetClinic.mapper.PetMapper;
import com.PetClinic.model.PetDTO;
import com.PetClinic.model.enums.PetType;
import com.PetClinic.repository.PetRepository;
import com.PetClinic.service.PetService;
import lombok.RequiredArgsConstructor;
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

    private final PetRepository petRepository;
    private final PetMapper petMapper;

    private static final int DEFAULT_PAGE_NUMBER = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;


    @Override
    public Page<PetDTO> listPets(String name, PetType petType, Integer age, Double weight, Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Pet> petPage;

        if (StringUtils.hasText(name) && petType == null) {
            petPage = listPetsByName(name, pageRequest);
        } else if (!StringUtils.hasText(name) && petType != null) {
            petPage = listPetsByType(petType, pageRequest);
        } else if (StringUtils.hasText(name) && petType != null) {
            petPage = listPetsByNameAndType(name, petType, pageRequest);
        } else {
            petPage = petRepository.findAll(pageRequest);
        }
        return petPage.map(petMapper::petToPetDto);
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
        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    public Page<Pet> listPetsByName(String name, Pageable pageable) {
        return petRepository.findAllByNameIsLikeIgnoreCase("%" + name + "%", pageable);
    }

    public Page<Pet> listPetsByType(PetType petType, Pageable pageable) {
        return petRepository.findAllByPetType(petType, pageable);
    }

    public Page<Pet> listPetsByNameAndType(String name, PetType petType, Pageable pageable) {
        return petRepository.findAllByNameIsLikeIgnoreCaseAndPetType("%" + name + "%", petType, pageable);
    }

    @Override
    public Optional<PetDTO> getById(UUID id) {
        return Optional.ofNullable(petMapper.petToPetDto(petRepository.findById(id).orElse(null)));
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
            foundPet.setAge(petDTO.getAge());
            foundPet.setWeight(petDTO.getWeight());
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
            if (StringUtils.hasText(petDTO.getName())) {
                foundPet.setName(petDTO.getName());
            }
            if (petDTO.getPetType() != null) {
                foundPet.setPetType(petDTO.getPetType());
            }
            if (petDTO.getAge() != null) {
                foundPet.setAge(petDTO.getAge());
            }
            if (petDTO.getWeight() != null) {
                foundPet.setWeight(petDTO.getWeight());
            }
            atomicReference.set(Optional.of(petMapper.petToPetDto(petRepository.save(foundPet))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }
}