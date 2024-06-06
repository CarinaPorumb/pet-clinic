package com.PetClinic.service;

import com.PetClinic.model.VisitDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisitService {

    List<VisitDTO> listVisits(LocalDate startDate, LocalDate endDate, String diagnosis, int pageNumber, int pageSize);

    Optional<VisitDTO> getVisitById(UUID id);

    VisitDTO saveNewVisit(VisitDTO visitDTO);

    Optional<VisitDTO> updateVisit(UUID id, VisitDTO visitDTO);

    boolean deleteVisitById(UUID id);

    Optional<VisitDTO> patchVisitById(UUID id, VisitDTO visitDTO);

}