package com.PetClinic.controller;

import com.PetClinic.entity.Pet;
import com.PetClinic.exception.NotFoundException;
import com.PetClinic.mapper.PetMapper;
import com.PetClinic.model.PetDTO;
import com.PetClinic.repository.PetRepository;
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

import static com.PetClinic.controller.PetController.PET_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PetControllerTest {

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
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void getPetById() {
        Pet pet = petRepository.findAll().get(0);
        PetDTO petDTO = petController.getPetById(pet.getId());
        assertThat(petDTO).isNotNull();
    }

    @Test
    void getVetByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            petController.getPetById(UUID.randomUUID());
        });
    }

    @Test
    void listPets() {
        List<PetDTO> dtos = petController.listPets();
        assertThat(dtos.size()).isEqualTo(2);
    }

    @Rollback
    @Transactional
    @Test
    void petEmptyList() {
        petRepository.deleteAll();
        List<PetDTO> dtos = petController.listPets();
        assertThat(dtos.size()).isEqualTo(0);
    }

    @Rollback
    @Transactional
    @Test
    void createNewVet() {
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
    void updateVetById() {
        Pet pet = petRepository.findAll().get(0);
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
    void updateVetNotFound() {
        assertThrows(NotFoundException.class, () -> {
            petController.updatePetById(UUID.randomUUID(), PetDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void deleteVetById() {
        Pet pet = petRepository.findAll().get(0);
        ResponseEntity<?> responseEntity = petController.deletePetById(pet.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
        assertThat(petRepository.findById(pet.getId())).isEmpty();
    }

    @Test
    void deleteVetByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            petController.deletePetById(UUID.randomUUID());
        });
    }

    @Test
    void patchVetBadName() throws Exception {
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