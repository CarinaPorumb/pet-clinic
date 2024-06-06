package com.PetClinic.controller;

import com.PetClinic.exception.NotFoundException;
import com.PetClinic.model.VetDTO;
import com.PetClinic.service.VetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class VetController {

    public static final String VET_PATH = "/api/v1/vet";
    public static final String VET_PATH_ID = VET_PATH + "/{id}";
    private final VetService vetService;

    @GetMapping(value = VET_PATH)
    public Page<VetDTO> listVets(String speciality, int pageNumber, int pageSize) {
        return vetService.listVets(speciality, pageNumber, pageSize);
    }

    @GetMapping(value = VET_PATH_ID)
    public VetDTO getVetById(@PathVariable("id") UUID id) {
        return vetService.getVetById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping(value = VET_PATH)
    public ResponseEntity<?> createNewVet(@Validated @RequestBody VetDTO vetDTO) {
        VetDTO savedVetDto = vetService.saveNewVet(vetDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/vet/" + savedVetDto.getId().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = VET_PATH_ID)
    public ResponseEntity<?> updateVetById(@PathVariable("id") UUID id, @Validated @RequestBody VetDTO vetDTO) {
        if (vetService.updateVet(id, vetDTO).isEmpty())
            throw new NotFoundException();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = VET_PATH_ID)
    public ResponseEntity<?> deleteVetById(@PathVariable("id") UUID id) {
        if (!vetService.deleteVetById(id))
            throw new NotFoundException();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = VET_PATH_ID)
    public ResponseEntity<?> patchVetById(@PathVariable("id") UUID id, @RequestBody VetDTO vetDTO) {
        vetService.patchVetById(id, vetDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}