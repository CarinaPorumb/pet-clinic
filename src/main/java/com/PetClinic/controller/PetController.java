package com.PetClinic.controller;

import com.PetClinic.exception.NotFoundException;
import com.PetClinic.model.PetDTO;
import com.PetClinic.model.enums.PetType;
import com.PetClinic.service.PetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/pet")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    public ResponseEntity<Page<PetDTO>> listPets(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") PetType petType,
            @RequestParam(required = false, defaultValue = "") Integer age,
            @RequestParam(required = false, defaultValue = "") Double weight,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        log.info("Request to list pets");
        return ResponseEntity.ok(petService.listPets(name, petType, age, weight, pageNumber, pageSize));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PetDTO> getPetById(@PathVariable("id") UUID id) {
        log.info("Request to get pet by ID: {}", id);
        return ResponseEntity.ok(petService.getPetById(id).orElseThrow(() -> new NotFoundException("Pet not found with id: " + id)));
    }

    @PostMapping
    public ResponseEntity<PetDTO> createNewPet(@Validated @RequestBody PetDTO petDTO) {
        log.info("Request to create new pet: {}", petDTO);
        PetDTO savedPetDto = petService.saveNewPet(petDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/pet/" + savedPetDto.getId().toString());
        return new ResponseEntity<>(savedPetDto, headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PetDTO> updatePetById(@PathVariable("id") UUID id, @Validated @RequestBody PetDTO petDTO) {
        log.info("Request to update pet by ID: {}", id);
        return ResponseEntity.ok(petService.updatePet(id, petDTO)
                .orElseThrow(() -> new NotFoundException("Pet not found with id: " + id)));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePetById(@PathVariable("id") UUID id) {
        log.info("Request to delete pet by ID: {}", id);
        boolean deleted = petService.deletePetById(id);
        if (!deleted) {
            throw new NotFoundException("Pet not found with id: " + id);
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<PetDTO> patchPetById(@PathVariable("id") UUID id, @RequestBody PetDTO petDTO) {
        log.info("Request to patch pet by ID: {}, with data: {}", id, petDTO);
        return ResponseEntity.ok(petService.patchPetById(id, petDTO)
                .orElseThrow(() -> new NotFoundException("Pet not found with id: " + id)));
    }

}