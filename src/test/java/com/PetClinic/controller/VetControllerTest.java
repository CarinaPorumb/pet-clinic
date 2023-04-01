package com.PetClinic.controller;

import com.PetClinic.entity.Vet;
import com.PetClinic.exception.NotFoundException;
import com.PetClinic.mapper.VetMapper;
import com.PetClinic.model.VetDTO;
import com.PetClinic.repository.VetRepository;
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

import static com.PetClinic.controller.VetController.VET_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class VetControllerTest {

    @Autowired
    VetController vetController;
    @Autowired
    VetRepository vetRepository;
    @Autowired
    VetMapper vetMapper;
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
    void getVetById() {
        Vet vet = vetRepository.findAll().get(0);
        VetDTO vetDTO = vetController.getVetById(vet.getId());
        assertThat(vetDTO).isNotNull();
    }

    @Test
    void getVetByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            vetController.getVetById(UUID.randomUUID());
        });
    }

    @Test
    void listVets() {
        List<VetDTO> dtos = vetController.listVets();
        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void vetEmptyList() {
        vetRepository.deleteAll();
        List<VetDTO> dtos = vetController.listVets();
        assertThat(dtos.size()).isEqualTo(0);
    }

    @Rollback
    @Transactional
    @Test
    void createNewVet() {
        VetDTO dto = VetDTO.builder().name("New Vet Name").build();
        ResponseEntity<?> responseEntity = vetController.createNewVet(dto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);
        Vet vet = vetRepository.findById(savedUUID).get();
        assertThat(vet).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void updateVetById() {
        Vet vet = vetRepository.findAll().get(0);
        VetDTO vetDTO = vetMapper.vetToVetDto(vet);
        vetDTO.setId(null);
        vetDTO.setSpeciality(null);
        final String vetName = "Updated";
        vetDTO.setName(vetName);

        ResponseEntity<?> responseEntity = vetController.updateVetById(vet.getId(), vetDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));

        Vet updatedVet = vetRepository.findById(vet.getId()).get();
        assertThat(updatedVet.getName()).isEqualTo(vetName);
    }

    @Test
    void updateVetNotFound() {
        assertThrows(NotFoundException.class, () -> {
            vetController.updateVetById(UUID.randomUUID(), VetDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void deleteVetById() {
        Vet vet = vetRepository.findAll().get(0);
        ResponseEntity<?> responseEntity = vetController.deleteVetById(vet.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.valueOf(204));
        assertThat(vetRepository.findById(vet.getId())).isEmpty();
    }

    @Test
    void deleteVetByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            vetController.deleteVetById(UUID.randomUUID());
        });
    }

    @Test
    void patchVetBadName() throws Exception {
        Vet vet = vetRepository.findAll().get(0);

        Map<String, Object> vetMap = new HashMap<>();
        vetMap.put("name", "New Name 0123456789012345678901234567890123456789012345678901234567890123456789");

        MvcResult result = mockMvc.perform(patch(VET_PATH_ID, vet.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vetMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}