package com.PetClinic.controller;

import com.PetClinic.exception.NotFoundException;
import com.PetClinic.model.OwnerDTO;
import com.PetClinic.service.OwnerService;
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
@RequestMapping("/api/v1/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    @GetMapping
    public ResponseEntity<Page<OwnerDTO>> listOwners(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String address,
            @RequestParam(required = false, defaultValue = "") String telephone,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("Request to list owners");
        return ResponseEntity.ok(ownerService.listOwners(name, address, telephone, page, size));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OwnerDTO> getOwnerById(@PathVariable("id") UUID id) {
        log.info("Request to get owner by ID: {}", id);
        return ResponseEntity.ok(ownerService.getOwnerById(id)
                .orElseThrow(() -> new NotFoundException("Owner not found with id: " + id)));
    }

    @PostMapping
    public ResponseEntity<OwnerDTO> createNewOwner(@Validated @RequestBody OwnerDTO ownerDTO) {
        log.info("Request to create new owner: {}", ownerDTO);
        OwnerDTO savedOwner = ownerService.saveNewOwner(ownerDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/owner/" + savedOwner.getId().toString());
        return new ResponseEntity<>(savedOwner, headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<OwnerDTO> updateOwnerById(@PathVariable("id") UUID id, @Validated @RequestBody OwnerDTO ownerDTO) {
        log.info("Request to update owner by ID: {}", id);
        return ResponseEntity.ok(ownerService.updateOwner(id, ownerDTO)
                .orElseThrow(() -> new NotFoundException("Owner not found with id: " + id)));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteOwnerById(@PathVariable("id") UUID id) {
        log.info("Request to delete owner by ID: {}", id);
        boolean deleted = ownerService.deleteOwnerById(id);
        if (!deleted) {
            throw new NotFoundException("Owner not found with id: " + id);
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> patchOwnerById(@PathVariable("id") UUID id, @RequestBody OwnerDTO ownerDTO) {
        log.info("Request to patch owner by ID: {}, with data: {}", id, ownerDTO);
        return ResponseEntity.ok(ownerService.patchOwnerById(id, ownerDTO).orElseThrow(() -> new NotFoundException("Owner not found with id: " + id)));
    }

}