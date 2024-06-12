package com.PetClinic.controller;

import com.PetClinic.exception.NotFoundException;
import com.PetClinic.model.VisitDTO;
import com.PetClinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/visit")
@RequiredArgsConstructor
public class VisitController {

    private final VisitService visitService;

    @GetMapping
    public ResponseEntity<Page<VisitDTO>> listVisits(
            @RequestParam(required = false, defaultValue = "") String diagnosis,
            @RequestParam(required = false, defaultValue = "") LocalDate date,
            @RequestParam(required = false, defaultValue = "") Integer price,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        log.info("Request to list visits");
        return ResponseEntity.ok(visitService.listVisits(diagnosis, date, price, pageNumber, pageSize));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<VisitDTO> getVisitById(@PathVariable("id") UUID id) {
        log.info("Request to get visit by id {}", id);
        return ResponseEntity.ok(visitService.getVisitById(id)
                .orElseThrow(() -> new NotFoundException("Visit not found with id " + id)));
    }

    @PostMapping
    public ResponseEntity<VisitDTO> createNewVisit(@Validated @RequestBody VisitDTO visitDTO) {
        log.info("Request to create new visit {}", visitDTO);
        VisitDTO savedVisit = visitService.saveNewVisit(visitDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/visit/" + savedVisit.getId().toString());
        return new ResponseEntity<>(savedVisit, headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<VisitDTO> updateVisitById(@PathVariable("id") UUID id, @Validated @RequestBody VisitDTO visitDTO) {
        log.info("Request to update visit by ID {}", id);
        return ResponseEntity.ok(visitService.updateVisit(id, visitDTO)
                .orElseThrow(() -> new NotFoundException("Visit not found with ID " + id)));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteVisitById(@PathVariable("id") UUID id) {
        log.info("Request to delete visit by id {}", id);
        boolean deleted = visitService.deleteVisitById(id);
        if (!deleted) {
            throw new NotFoundException("Visit not found with id " + id);
        }
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<VisitDTO> patchVisitById(@PathVariable("id") UUID id, @RequestBody VisitDTO visitDTO) {
        log.info("Request to patch visit by ID: {}, with data: {}", id, visitDTO);
        return ResponseEntity.ok(visitService.patchVisitById(id, visitDTO)
                .orElseThrow(() -> new NotFoundException("Visit not found with id: " + id)));
    }

}