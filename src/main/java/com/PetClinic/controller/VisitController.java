package com.PetClinic.controller;

import com.PetClinic.exception.NotFoundException;
import com.PetClinic.model.VisitDTO;
import com.PetClinic.service.VisitService;
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
public class VisitController {

    public static final String VISIT_PATH = "/api/v1/visit";
    public static final String VISIT_PATH_ID = VISIT_PATH + "/{id}";
    private final VisitService visitService;

    @GetMapping(value = VISIT_PATH)
    public List<VisitDTO> listVisits() {
        return visitService.listVisits();
    }

    @GetMapping(value = VISIT_PATH_ID)
    public VisitDTO getVisitById(@PathVariable("id") UUID id) {
        return visitService.getById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping(value = VISIT_PATH)
    public ResponseEntity<?> createNewVisit(@Validated @RequestBody VisitDTO visitDTO) {
        VisitDTO savedVisit = visitService.saveNewVisit(visitDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/visit/" + savedVisit.getId().toString());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = VISIT_PATH_ID)
    public ResponseEntity<?> updateVisitById(@PathVariable("id") UUID id, @Validated @RequestBody VisitDTO visitDTO) {
        if (visitService.updateVisit(id, visitDTO).isEmpty())
            throw new NotFoundException();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = VISIT_PATH_ID)
    public ResponseEntity<?> deleteVisitById(@PathVariable("id") UUID id) {
        if (!visitService.deleteById(id))
            throw new NotFoundException();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(value = VISIT_PATH_ID)
    public ResponseEntity<?> patchVisitById(@PathVariable("id") UUID id, @RequestBody VisitDTO visitDTO) {
        visitService.patchById(id, visitDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}