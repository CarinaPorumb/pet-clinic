//package com.PetClinic.service.impl;
//
//import com.PetClinic.model.VisitDTO;
//import com.PetClinic.service.VisitService;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import java.time.LocalDate;
//import java.util.*;
//
//@Service
//public class VisitServiceMapImpl implements VisitService {
//
//    private Map<UUID, VisitDTO> visitDTOMap;
//
//    @Override
//    public List<VisitDTO> listVisits(LocalDate startDate, LocalDate endDate, String diagnosis, int pageNumber, int pageSize) {
//        return new ArrayList<>(visitDTOMap.values());
//    }
//
//    @Override
//    public Optional<VisitDTO> getVisitById(UUID id) {
//        return Optional.of(visitDTOMap.get(id));
//    }
//
//    @Override
//    public VisitDTO saveNewVisit(VisitDTO visitDTO) {
//        VisitDTO savedVisitDTO = VisitDTO.builder()
//                .id(UUID.randomUUID())
//                .diagnosis(visitDTO.getDiagnosis())
//                .price(visitDTO.getPrice())
//                .date(LocalDate.now())
//                .build();
//        visitDTOMap.put(savedVisitDTO.getId(), savedVisitDTO);
//        return savedVisitDTO;
//    }
//
//    @Override
//    public Optional<VisitDTO> updateVisit(UUID id, VisitDTO visitDTO) {
//        VisitDTO existing = visitDTOMap.get(id);
//        existing.setDiagnosis(visitDTO.getDiagnosis());
//        existing.setPrice(visitDTO.getPrice());
//        existing.setDate(visitDTO.getDate());
//        return Optional.of(existing);
//    }
//
//    @Override
//    public boolean deleteVisitById(UUID id) {
//        visitDTOMap.remove(id);
//        return true;
//    }
//
//    @Override
//    public Optional<VisitDTO> patchVisitById(UUID id, VisitDTO visitDTO) {
//        VisitDTO existing = visitDTOMap.get(id);
//
//        if (StringUtils.hasText(visitDTO.getDiagnosis())) {
//            existing.setDiagnosis(visitDTO.getDiagnosis());
//        }
//        if (visitDTO.getPrice() != null) {
//            existing.setPrice(visitDTO.getPrice());
//        }
//        if (visitDTO.getDate() != null) {
//            existing.setDate(visitDTO.getDate());
//        }
//        return Optional.of(existing);
//    }
//}
