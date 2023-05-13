package com.PetClinic.controller;

import com.PetClinic.config.SpringSecurityConfig;
import com.PetClinic.model.PetDTO;
import com.PetClinic.service.PetService;
import com.PetClinic.service.impl.PetServiceMapImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.PetClinic.controller.PetController.PET_PATH;
import static com.PetClinic.controller.PetController.PET_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
@Import(SpringSecurityConfig.class)
public class PetControllerTest {

    public static final String USERNAME = "userPostman";
    public static final String PASSWORD = "passwordPostman";

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    PetService petService;

    PetServiceMapImpl petServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<PetDTO> petDTOArgumentCaptor;

    @BeforeEach
    void setUp() {
        petServiceImpl = new PetServiceMapImpl();
    }

    @Test
    void getPetByid() throws Exception {
        PetDTO testPet = petServiceImpl.listPets(null, null, null, null, 1, 25).getContent().get(0);

        given(petService.getById(testPet.getId())).willReturn(Optional.of(testPet));

        mockMvc.perform(get(PET_PATH_ID, testPet.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testPet.getId().toString())))
                .andExpect(jsonPath("$.name", is(testPet.getName())));

        verify(petService).getById(uuidArgumentCaptor.capture());
        assertThat(testPet.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void getPetIdNotFound() throws Exception {
        given(petService.getById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(PET_PATH_ID, UUID.randomUUID())
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().isNotFound());
    }

    @Test
    void listPets() throws Exception {
        given(petService.listPets(any(), any(), any(), any(), any(), any()))
                .willReturn(petServiceImpl.listPets(null, null, null, null, null, null));

        mockMvc.perform(get(PET_PATH)
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(11)));
    }

    @Test
    void createNewPet() throws Exception {
        PetDTO testPet = petServiceImpl.listPets(null, null, null, null, 1, 25).getContent().get(0);
        testPet.setWeight(null);
        testPet.setId(null);

        given(petService.saveNewPet(any(PetDTO.class))).willReturn(petServiceImpl.listPets(null, null, null, null, 1, 25).getContent().get(1));

        mockMvc.perform(post(PET_PATH)
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPet)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

        System.out.println(objectMapper.writeValueAsString(testPet));
    }

    @Test
    void createPetNullPetName() throws Exception {
        PetDTO testPet = PetDTO.builder().build();

        given(petService.saveNewPet(any(PetDTO.class))).willReturn(petServiceImpl.listPets(null, null, null, null, 1, 25).getContent().get(1));

        MvcResult mvcResult = mockMvc.perform(post(PET_PATH)
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPet)))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(status().isBadRequest()).andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void updatePet() throws Exception {
        PetDTO testPet = petServiceImpl.listPets(null, null, null, null, 1, 25).getContent().get(0);
        given(petService.updatePet(any(), any())).willReturn(Optional.of(testPet));

        mockMvc.perform(put(PET_PATH_ID, testPet.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPet)))
                .andExpect(status().isNoContent());

        verify(petService).updatePet(uuidArgumentCaptor.capture(), any(PetDTO.class));
        assertThat(testPet.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void updatePetBlankName() throws Exception {
        PetDTO testPet = petServiceImpl.listPets(null, null, null, null, 1, 25).getContent().get(0);
        testPet.setName("");

        given(petService.updatePet(any(), any())).willReturn(Optional.of(testPet));

        mockMvc.perform(put(PET_PATH_ID, testPet.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testPet)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));

        System.out.println(objectMapper.writeValueAsString(testPet));

    }

    @Test
    void deletePet() throws Exception {
        PetDTO testPet = petServiceImpl.listPets(null, null, null, null, 1, 25).getContent().get(0);

        given(petService.deleteById(any())).willReturn(true);

        mockMvc.perform(delete(PET_PATH_ID, testPet.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(petService).deleteById(uuidArgumentCaptor.capture());
        assertThat(testPet.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void patchPet() throws Exception {
        PetDTO testPet = petServiceImpl.listPets(null, null, null, null, 1, 25).getContent().get(0);
        given(petService.patchById(any(), any())).willReturn(Optional.of(testPet));

        Map<String, Object> petMap = new HashMap<>();
        petMap.put("name", "newName");

        mockMvc.perform(patch(PET_PATH_ID, testPet.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petMap)))
                .andExpect(status().isNoContent());

        verify(petService).patchById(uuidArgumentCaptor.capture(), petDTOArgumentCaptor.capture());
        assertThat(testPet.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(petMap.get("name")).isEqualTo(petDTOArgumentCaptor.getValue().getName());
    }

}
