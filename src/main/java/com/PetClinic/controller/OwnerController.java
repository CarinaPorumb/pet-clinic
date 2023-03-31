package com.PetClinic.controller;

import com.PetClinic.exception.NotFoundException;
import com.PetClinic.model.OwnerDTO;
import com.PetClinic.service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OwnerController {

    public static final String OWNER_PATH = "/api/v1/owner";
    public static final String OWNER_PATH_ID = OWNER_PATH + "/{id}";
    private final OwnerService ownerService;

    @GetMapping
    public OwnerDTO getOwnerById(@PathVariable("id") UUID id) {
        return ownerService.getById(id).orElseThrow(NotFoundException::new);
    }

    @GetMapping(OWNER_PATH)
    public List<OwnerDTO> listOwners() {
        return ownerService.listOwners();
    }

    @PostMapping(OWNER_PATH)
    public ResponseEntity<?> createNewOwner(@Validated @RequestBody OwnerDTO ownerDTO) {
        OwnerDTO savedOwner = ownerService.saveNewOwner(ownerDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/owner/" + savedOwner.getId().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(OWNER_PATH_ID)
    public ResponseEntity<?> updateOwnerById(@PathVariable("id") UUID id, @Validated @RequestBody OwnerDTO ownerDTO) {
        if (ownerService.updateOwner(id, ownerDTO).isEmpty())
            throw new NotFoundException();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(OWNER_PATH_ID)
    public ResponseEntity<?> deleteOwnerById(@PathVariable("id") UUID id) {
        if (!ownerService.deleteById(id))
            throw new NotFoundException();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(OWNER_PATH_ID)
    public ResponseEntity<?> patchOwnerById(@PathVariable("id") UUID id, @RequestBody OwnerDTO ownerDTO) {
        ownerService.patchById(id, ownerDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
