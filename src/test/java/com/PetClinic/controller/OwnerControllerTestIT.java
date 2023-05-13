package com.PetClinic.controller;

import com.PetClinic.entity.Owner;
import com.PetClinic.exception.NotFoundException;
import com.PetClinic.mapper.OwnerMapper;
import com.PetClinic.repository.OwnerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.PetClinic.controller.OwnerController.OWNER_PATH_ID;
import static com.PetClinic.controller.PetControllerTest.jwt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class OwnerControllerTestIT {

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
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    void getOwnerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            ownerController.getOwnerById(UUID.randomUUID());
        });
    }

    @Test
    void getOwnerById() {
        Owner owner = ownerRepository.findAll().get(0);
        com.PetClinic.model.OwnerDTO ownerDto = ownerController.getOwnerById(owner.getId());
        assertThat(ownerDto).isNotNull();
    }

    @Test
    void listOwners() {
        List<com.PetClinic.model.OwnerDTO> dtos = ownerController.listOwners();
        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void ownerEmptyList() {
        ownerRepository.deleteAll();
        List<com.PetClinic.model.OwnerDTO> dtos = ownerController.listOwners();
        assertThat(dtos.size()).isEqualTo(0);
    }

    @Rollback
    @Transactional
    @Test
    void createNewOwner() {
        com.PetClinic.model.OwnerDTO ownerDTO = com.PetClinic.model.OwnerDTO.builder().name("New owner name").build();
        ResponseEntity<?> responseEntity = ownerController.createNewOwner(ownerDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);
        Owner owner = ownerRepository.findById(savedUUID).get();
        assertThat(owner).isNotNull();
    }

    @Test
    void updateOwnerNotFound() {
        assertThrows(NotFoundException.class, () -> {
            ownerController.updateOwnerById(UUID.randomUUID(), com.PetClinic.model.OwnerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void updateOwnerById() {
        Owner owner = ownerRepository.findAll().get(0);
        com.PetClinic.model.OwnerDTO ownerDTO = ownerMapper.ownerToOwnerDto(owner);
        ownerDTO.setId(null);
        final String ownerName = "Updated";
        ownerDTO.setName(ownerName);

        ResponseEntity<?> responseEntity = ownerController.updateOwnerById(owner.getId(), ownerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));

        Owner updatedOwner = ownerRepository.findById(owner.getId()).get();
        assertThat(updatedOwner.getName()).isEqualTo(ownerName);
    }

    @Test
    void deleteOwnerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            ownerController.deleteOwnerById(UUID.randomUUID());
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
    void patchOwnerAttributeTooLong() throws Exception {
        Owner owner = ownerRepository.findAll().get(0);
        Map<String, Object> ownerMap = new HashMap<>();

        ownerMap.put("telephone", "0123456789012345678901234567890123456789012345678901234567890123456789");

        MvcResult result = mockMvc.perform(patch(OWNER_PATH_ID, owner.getId())
                        .with(jwt)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ownerMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

}