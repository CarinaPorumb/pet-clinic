package com.PetClinic.service;

import com.PetClinic.model.VisitDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface VisitService {

    Page<VisitDTO> listVisits(String diagnosis, LocalDate date, Integer price, Integer pageNumber, Integer pageSize);

    Optional<VisitDTO> getVisitById(UUID id);

    VisitDTO saveNewVisit(VisitDTO visitDTO);

    Optional<VisitDTO> updateVisit(UUID id, VisitDTO visitDTO);

    boolean deleteVisitById(UUID id);

    Optional<VisitDTO> patchVisitById(UUID id, VisitDTO visitDTO);

}