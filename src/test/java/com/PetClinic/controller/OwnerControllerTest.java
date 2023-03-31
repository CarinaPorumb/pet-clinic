package com.PetClinic.controller;

import com.PetClinic.entity.Owner;
import com.PetClinic.exception.NotFoundException;
import com.PetClinic.mapper.OwnerMapper;
import com.PetClinic.model.OwnerDTO;
import com.PetClinic.repository.OwnerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class OwnerControllerTest {

    @Autowired
    OwnerController ownerController;
    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    OwnerMapper ownerMapper;
    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void getOwnerById() {
        Owner owner = ownerRepository.findAll().get(0);
        OwnerDTO ownerDto = ownerController.getOwnerById(owner.getId());
        assertThat(ownerDto).isNotNull();
    }

    @Test
    void getOwnerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            ownerController.getOwnerById(UUID.randomUUID());
        });

    }

    @Test
    void listOwners() {
        List<OwnerDTO> dtos = ownerController.listOwners();
        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void ownerEmptyList() {
        ownerRepository.deleteAll();
        List<OwnerDTO> dtos = ownerController.listOwners();
        assertThat(dtos.size()).isEqualTo(0);
    }

    @Rollback
    @Transactional
    @Test
    void createNewOwner() {
        OwnerDTO ownerDTO = OwnerDTO.builder().name("New Owner").build();
        ResponseEntity<?> responseEntity = ownerController.createNewOwner(ownerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);
        Owner owner = ownerRepository.findById(savedUUID).get();
        assertThat(owner).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void updateOwnerById() {
        Owner owner = ownerRepository.findAll().get(0);
        OwnerDTO ownerDTO = ownerMapper.ownerToOwnerDto(owner);
        ownerDTO.setId(null);
        final String ownerName = "Updated";
        ownerDTO.setName(ownerName);

        ResponseEntity<?> responseEntity = ownerController.updateOwnerById(owner.getId(), ownerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));

        Owner updateOwner = ownerRepository.findById(owner.getId()).get();
        assertThat(updateOwner.getName()).isEqualTo(ownerName);
    }

    @Test
    void updateOwnerNotFound() {
        assertThrows(NotFoundException.class, () -> {
            ownerController.updateOwnerById(UUID.randomUUID(), OwnerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void deleteOwnerById() {
        Owner owner = ownerRepository.findAll().get(0);
        ResponseEntity<?> responseEntity = ownerController.deleteOwnerById(owner.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
        assertThat(ownerRepository.findById(owner.getId())).isEmpty();
    }

    @Test
    void deleteIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            ownerController.deleteOwnerById(UUID.randomUUID());
        });
    }

}