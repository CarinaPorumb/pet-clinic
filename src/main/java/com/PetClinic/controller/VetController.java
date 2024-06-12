package com.PetClinic.controller;

import com.PetClinic.exception.NotFoundException;
import com.PetClinic.model.VetDTO;
import com.PetClinic.service.VetService;
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
@RequestMapping("/api/v1/vet")
@RequiredArgsConstructor
public class VetController {

    private final VetService vetService;

    @GetMapping
    public ResponseEntity<Page<VetDTO>> listVets(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String speciality,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        log.info("Request to list vets");
        return ResponseEntity.ok(vetService.listVets(name, speciality, pageNumber, pageSize));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<VetDTO> getVetById(@PathVariable("id") UUID id) {
        log.info("Request to get Vet by id {}", id);
        return ResponseEntity.ok(vetService.getVetById(id)
                .orElseThrow(() -> new NotFoundException("Vet not found with id " + id)));
    }

    @PostMapping
    public ResponseEntity<VetDTO> createNewVet(@Validated @RequestBody VetDTO vetDTO) {
        log.info("Request to create new vet: {}", vetDTO);
        VetDTO savedVetDto = vetService.saveNewVet(vetDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/vet/" + savedVetDto.getId().toString());
        return new ResponseEntity<>(savedVetDto, headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<VetDTO> updateVetById(@PathVariable("id") UUID id, @Validated @RequestBody VetDTO vetDTO) {
        log.info("Request to update vet by ID: {}", id);
        return ResponseEntity.ok(vetService.updateVet(id, vetDTO)
                .orElseThrow(() -> new NotFoundException("Vet not found with id: " + id)));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteVetById(@PathVariable("id") UUID id) {
        log.info("Request to delete vet by id {}", id);
        boolean deleted = vetService.deleteVetById(id);
        if (!deleted)
            throw new NotFoundException("Vet not found with id " + id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<VetDTO> patchVetById(@PathVariable("id") UUID id, @RequestBody VetDTO vetDTO) {
        log.info("Request to patch vet by ID: {}, with data: {}", id, vetDTO);
        vetService.patchVetById(id, vetDTO);
        return ResponseEntity.ok(vetService.patchVetById(id, vetDTO).orElseThrow(() -> new NotFoundException("Vet not found with id: " + id)));
    }

}