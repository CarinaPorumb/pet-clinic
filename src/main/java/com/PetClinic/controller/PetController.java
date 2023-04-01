package com.PetClinic.controller;

import com.PetClinic.exception.NotFoundException;
import com.PetClinic.model.PetDTO;
import com.PetClinic.service.PetService;
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
public class PetController {

    public static final String PET_PATH = "/api/v1/pet";
    public static final String PET_PATH_ID = PET_PATH + "/{id}";
    private final PetService petService;

    @GetMapping(PET_PATH_ID)
    public PetDTO getPetById(@PathVariable("id") UUID id) {
        return petService.getById(id).orElseThrow(NotFoundException::new);
    }

    @GetMapping(PET_PATH)
    public List<PetDTO> listPets() {
        return petService.listPets();
    }

    @PostMapping(PET_PATH)
    public ResponseEntity<?> createNewPet(@Validated @RequestBody PetDTO petDTO) {
        PetDTO savedPetDto = petService.saveNewPet(petDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/pet/" + savedPetDto.getId().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(PET_PATH_ID)
    public ResponseEntity<?> updatePetById(@PathVariable("id") UUID id, @Validated @RequestBody PetDTO petDTO) {
        if (petService.updatePet(id, petDTO).isEmpty())
            throw new NotFoundException();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(PET_PATH_ID)
    public ResponseEntity<?> deletePetById(@PathVariable("id") UUID id) {
        if (!petService.deleteById(id))
            throw new NotFoundException();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(PET_PATH_ID)
    public ResponseEntity<?> patchPetById(@PathVariable("id") UUID id, @RequestBody PetDTO petDTO) {
        petService.patchById(id, petDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}