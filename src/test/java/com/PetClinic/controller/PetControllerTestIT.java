package com.PetClinic.controller;

import com.PetClinic.entity.Pet;
import com.PetClinic.exception.NotFoundException;
import com.PetClinic.mapper.PetMapper;
import com.PetClinic.model.PetDTO;
import com.PetClinic.model.enums.PetType;
import com.PetClinic.repository.PetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
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
import java.util.Map;
import java.util.UUID;

import static com.PetClinic.controller.PetController.PET_PATH;
import static com.PetClinic.controller.PetController.PET_PATH_ID;
import static com.PetClinic.controller.PetControllerTest.PASSWORD;
import static com.PetClinic.controller.PetControllerTest.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PetControllerTestIT {

    @Autowired
    PetController petController;
    @Autowired
    PetRepository petRepository;
    @Autowired
    PetMapper petMapper;
    @Autowired
    ObjectMapper objectMapper;
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
    void testNoAuth() throws Exception {
        mockMvc.perform(get(PET_PATH)
                        .queryParam("petType", PetType.BIRD.name()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getPetById() {
        Pet pet = petRepository.findAll().get(0);
        PetDTO petDTO = petController.getPetById(pet.getId());
        assertThat(petDTO).isNotNull();
    }

    @Test
    void getPetByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            petController.getPetById(UUID.randomUUID());
        });
    }

    @Test
    void listPets() {
        Page<PetDTO> dtos = petController.listPets(null, null, null, null, null, null);
        assertThat(dtos.getContent().size()).isEqualTo(25);
    }

    @Test
    void listPetsByName() throws Exception {
        mockMvc.perform(get(PET_PATH)
                        .with(httpBasic(USERNAME, PASSWORD))
                        .queryParam("name", "Rocco"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(11)));
    }

    @Test
    void listPetsByType() throws Exception {
        mockMvc.perform(get(PET_PATH)
                        .with(httpBasic(USERNAME, PASSWORD))
                        .queryParam("petType", PetType.CAT.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(11)));
    }

    @Test
    void listPetsByNameAndType() throws Exception {
        mockMvc.perform(get(PET_PATH)
                        .with(httpBasic(USERNAME, PASSWORD))
                        .queryParam("name", "Jarrus")
                        .queryParam("petType", PetType.DOG.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(11)));
    }

    @Rollback
    @Transactional
    @Test
    void petEmptyList() {
        petRepository.deleteAll();
        Page<PetDTO> dtos = petController.listPets(null,null, null, null, null, null);
        assertThat(dtos.getContent().size()).isEqualTo(0);
    }



    @Rollback
    @Transactional
    @Test
    void createNewPet() {
        PetDTO dto = PetDTO.builder().name("New Pet Name").build();
        ResponseEntity<?> responseEntity = petController.createNewPet(dto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);
        Pet pet = petRepository.findById(savedUUID).get();
        assertThat(pet).isNotNull();
    }


    @Rollback
    @Transactional
    @Test
    void updatePetById() {
        Pet pet = petRepository.findAll().get(1);
        PetDTO petDTO = petMapper.petToPetDto(pet);
        petDTO.setId(null);
        final String petName = "Updated";
        petDTO.setName(petName);

        ResponseEntity<?> responseEntity = petController.updatePetById(pet.getId(), petDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));

        Pet updatedPet = petRepository.findById(pet.getId()).get();
        assertThat(updatedPet.getName()).isEqualTo(petName);
    }

    @Test
    void updatePetNotFound() {
        assertThrows(NotFoundException.class, () -> {
            petController.updatePetById(UUID.randomUUID(), PetDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void deletePetById() {
        Pet pet = petRepository.findAll().get(0);
        ResponseEntity<?> responseEntity = petController.deletePetById(pet.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
        assertThat(petRepository.findById(pet.getId())).isEmpty();
    }

    @Test
    void deletePetByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            petController.deletePetById(UUID.randomUUID());
        });
    }

    @Test
    void patchPetBadName() throws Exception {
        Pet pet = petRepository.findAll().get(0);

        Map<String, Object> petMap = new HashMap<>();
        petMap.put("name", "New Name 0123456789012345678901234567890123456789012345678901234567890123456789");

        MvcResult result = mockMvc.perform(patch(PET_PATH_ID, pet.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

}