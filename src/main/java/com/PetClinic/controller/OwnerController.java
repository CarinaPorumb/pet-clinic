package com.PetClinic.controller;

import com.PetClinic.exception.NotFoundException;
import com.PetClinic.model.OwnerDTO;
import com.PetClinic.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OwnerController {

    private static final Logger log = LoggerFactory.getLogger(OwnerController.class);
    public static final String OWNER_PATH = "/api/v1/owner";
    public static final String OWNER_PATH_ID = OWNER_PATH + "/{id}";
    private final OwnerService ownerService;

    @GetMapping(value = OWNER_PATH)
    public Page<OwnerDTO> listOwners(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        log.info("Request to list all owners at page {} with size {}", page, size);
        return ownerService.listOwners(page, size);
    }

    @GetMapping(value = OWNER_PATH_ID)
    public OwnerDTO getOwnerById(@PathVariable("id") UUID id) {
        log.info("Request to get owner by ID: {}", id);
        return ownerService.getById(id).orElseThrow(() -> new NotFoundException("Owner not found with id: " + id));
    }

    @PostMapping(value = OWNER_PATH)
    public ResponseEntity<?> createNewOwner(@Validated @RequestBody OwnerDTO ownerDTO) {
        OwnerDTO savedOwner = ownerService.saveNewOwner(ownerDTO);
        log.info("Request to create new owner: {}", ownerDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/owner/" + savedOwner.getId().toString());
        return new ResponseEntity<>(savedOwner, headers, HttpStatus.CREATED);
    }

    @PutMapping(value = OWNER_PATH_ID)
    public ResponseEntity<?> updateOwnerById(@PathVariable("id") UUID id, @Validated @RequestBody OwnerDTO ownerDTO) {
        log.info("Request to update owner by ID: {}", id);
        ownerService.updateOwner(id, ownerDTO).orElseThrow(() -> new NotFoundException("Owner not found with id: " + id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = OWNER_PATH_ID)
    public ResponseEntity<?> deleteOwnerById(@PathVariable("id") UUID id) {
        log.info("Request to delete owner by ID: {}", id);
        if (!ownerService.deleteById(id))
            throw new NotFoundException("Owner not found with id: " + id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = OWNER_PATH_ID)
    public ResponseEntity<?> patchOwnerById(@PathVariable("id") UUID id, @RequestBody OwnerDTO ownerDTO) {
        log.info("Request to patch owner by ID: {}, with data: {}", id, ownerDTO);
        ownerService.patchById(id, ownerDTO).orElseThrow(() -> new NotFoundException("Owner not found with id: " + id));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}