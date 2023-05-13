package com.PetClinic.controller;

import com.PetClinic.config.SpringSecurityConfig;
import com.PetClinic.model.OwnerDTO;
import com.PetClinic.service.OwnerService;
import com.PetClinic.service.impl.OwnerServiceMapImpl;
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

import static com.PetClinic.controller.OwnerController.OWNER_PATH;
import static com.PetClinic.controller.OwnerController.OWNER_PATH_ID;
import static com.PetClinic.controller.PetControllerTest.PASSWORD;
import static com.PetClinic.controller.PetControllerTest.USERNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OwnerController.class)
@Import(SpringSecurityConfig.class)
class OwnerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OwnerService ownerService;

    OwnerServiceMapImpl ownerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<OwnerDTO> ownerDTOArgumentCaptor;

    @BeforeEach
    void setUp() {
        ownerServiceImpl = new OwnerServiceMapImpl();
    }

    @Test
    void getOwnerById() throws Exception {
        OwnerDTO testOwner = ownerServiceImpl.listOwners().get(0);
        given(ownerService.getById(testOwner.getId())).willReturn(Optional.of(testOwner));

        mockMvc.perform(get(OWNER_PATH_ID, testOwner.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testOwner.getId().toString())))
                .andExpect(jsonPath("$.name", is(testOwner.getName())));

        verify(ownerService).getById(uuidArgumentCaptor.capture());
        assertThat(testOwner.getId()).isEqualTo(uuidArgumentCaptor.getValue());

    }

    @Test
    void getOwnerByIdNotFound() throws Exception {
        given(ownerService.getById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(OWNER_PATH_ID, UUID.randomUUID())
                        .with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().isNotFound());
    }

    @Test
    void listOwners() throws Exception {
        given(ownerService.listOwners()).willReturn(ownerServiceImpl.listOwners());

        mockMvc.perform(get(OWNER_PATH)
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void createNewOwner() throws Exception {
        OwnerDTO ownerDTO = ownerServiceImpl.listOwners().get(0);
        ownerDTO.setId(null);

        given(ownerService.saveNewOwner(any(OwnerDTO.class))).willReturn(ownerServiceImpl.listOwners().get(1));

        mockMvc.perform(post(OWNER_PATH)
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ownerDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void createOwnerNullName() throws Exception {
        OwnerDTO ownerDTO = OwnerDTO.builder().id(UUID.randomUUID()).telephone("12345").build();

        given(ownerService.saveNewOwner(any(OwnerDTO.class))).willReturn(ownerServiceImpl.listOwners().get(1));

        MvcResult mvcResult = mockMvc.perform(post(OWNER_PATH)
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ownerDTO)))
                .andExpect(jsonPath("$.length()", is(3)))
                .andExpect(status().isBadRequest()).andReturn();

        //System.out.println(mvcResult.getResponse().getContentAsString());
    }

    @Test
    void updateOwner() throws Exception {
        OwnerDTO ownerDTO = ownerServiceImpl.listOwners().get(0);
        given(ownerService.updateOwner(any(), any())).willReturn(Optional.of(OwnerDTO.builder().build()));
        mockMvc.perform(put(OWNER_PATH_ID, ownerDTO.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ownerDTO)))
                .andExpect(status().isNoContent());
        verify(ownerService).updateOwner(uuidArgumentCaptor.capture(), any(OwnerDTO.class));
        assertThat(ownerDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());

    }

    @Test
    void updateOwnerBlankName() throws Exception {
        OwnerDTO ownerDTO = ownerServiceImpl.listOwners().get(0);
        ownerDTO.setName("");

        given(ownerService.updateOwner(any(), any())).willReturn(Optional.of(ownerDTO));

        mockMvc.perform(put(OWNER_PATH_ID, ownerDTO.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ownerDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)));
    }

    @Test
    void deleteOwnerById() throws Exception {
        OwnerDTO ownerDTO = ownerServiceImpl.listOwners().get(0);
        given(ownerService.deleteById(any())).willReturn(true);

        mockMvc.perform(delete(OWNER_PATH_ID, ownerDTO.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNoContent());

        verify(ownerService).deleteById(uuidArgumentCaptor.capture());
        assertThat(ownerDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void patchOwner() throws Exception {
        OwnerDTO ownerDTO = ownerServiceImpl.listOwners().get(0);
        given(ownerService.patchById(any(), any())).willReturn(Optional.of(ownerDTO));

        Map<String, Object> ownerMap = new HashMap<>();
        ownerMap.put("name", "New Name");

        mockMvc.perform(patch(OWNER_PATH_ID, ownerDTO.getId())
                        .with(httpBasic(USERNAME, PASSWORD))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ownerMap)))
                .andExpect(status().isNoContent());

        verify(ownerService).patchById(uuidArgumentCaptor.capture(), ownerDTOArgumentCaptor.capture());
        assertThat(ownerDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(ownerMap.get("name")).isEqualTo(ownerDTOArgumentCaptor.getValue().getName());

    }

}