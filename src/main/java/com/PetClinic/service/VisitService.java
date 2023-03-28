package com.PetClinic.service;

import com.PetClinic.model.VisitDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VisitService {

    Optional<VisitDTO> getById(UUID id);

    List<VisitDTO> listVisits();

    VisitDTO saveNewVisit(VisitDTO visitDTO);

    Optional<VisitDTO> updateVisit(UUID id, VisitDTO visitDTO);

    boolean deleteById(UUID id);

    Optional<VisitDTO> patchById(UUID id, VisitDTO visitDTO);

}