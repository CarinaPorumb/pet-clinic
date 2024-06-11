package com.PetClinic.service.impl;

import com.PetClinic.entity.Visit;
import com.PetClinic.mapper.VisitMapper;
import com.PetClinic.model.VisitDTO;
import com.PetClinic.repository.VisitRepository;
import com.PetClinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Primary
@RequiredArgsConstructor
@Service
public class VisitServiceJpaImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<VisitDTO> listVisits(String diagnosis, LocalDate date, Integer price, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("date").ascending());

        Specification<Visit> visitSpecification = Specification.where(SpecificationUtils.<Visit>attributeLike("diagnosis", diagnosis))
                .and(SpecificationUtils.attributeEquals("date", date))
                .and(SpecificationUtils.attributeEquals("price", price));

        log.debug("Listing visits with filters - Diagnosis: " + diagnosis + ", Date: " + date + ", Price: " + price);
        return visitRepository.findAll(visitSpecification, pageable).map(visitMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VisitDTO> getVisitById(UUID id) {
        log.debug("Attempting to find visit with id {}", id);
        return visitRepository.findById(id).map(visitMapper::toDTO);
    }

    @Override
    @Transactional
    public VisitDTO saveNewVisit(VisitDTO visitDTO) {
        log.debug("Saving new visit: {}", visitDTO);
        Visit savedVisit = visitRepository.save(visitMapper.toEntity(visitDTO));
        log.info("Successfully saved new visit with id: {}", savedVisit.getId());
        return visitMapper.toDTO(savedVisit);
    }

    @Override
    @Transactional
    public Optional<VisitDTO> updateVisit(UUID id, VisitDTO visitDTO) {
        log.debug("Updating visit with id: {}", id);

        return visitRepository.findById(id)
                .map(existingVisit -> {
                    existingVisit.setDiagnosis(visitDTO.getDiagnosis());
                    existingVisit.setDate(visitDTO.getDate());
                    existingVisit.setPrice(visitDTO.getPrice());
                    visitRepository.save(existingVisit);
                    log.info("Visit updated with id: {}", existingVisit.getId());
                    return visitMapper.toDTO(existingVisit);
                });
    }

    @Override
    @Transactional
    public boolean deleteVisitById(UUID id) {
        log.debug("Attempting to delete visit with ID: {}", id);

        return visitRepository.findById(id)
                .map(visit -> {
                    visitRepository.delete(visit);
                    log.info("Deleted visit with ID: {}", id);
                    return true;
                }).orElse(false);
    }

    @Override
    @Transactional
    public Optional<VisitDTO> patchVisitById(UUID id, VisitDTO visitDTO) {
        log.debug("Patching visit with id {}", id);

        return visitRepository.findById(id).map(existingVisit -> {
            boolean isUpdated = false;

            if (StringUtils.hasText(visitDTO.getDiagnosis()) && !existingVisit.getDiagnosis().equals(visitDTO.getDiagnosis())) {
                existingVisit.setDiagnosis(visitDTO.getDiagnosis());
                isUpdated = true;
            }
            if (visitDTO.getDate() != null && !existingVisit.getDate().equals(visitDTO.getDate())) {
                existingVisit.setDate(visitDTO.getDate());
                isUpdated = true;
            }
            if (visitDTO.getPrice() != null && !existingVisit.getPrice().equals(visitDTO.getPrice())) {
                existingVisit.setPrice(visitDTO.getPrice());
                isUpdated = true;
            }

            if (isUpdated) {
                visitRepository.save(existingVisit);
                log.info("Visit with ID: {} patched successfully", id);
            } else {
                log.info("No changes applied to visit with id {}", id);
            }
            return visitMapper.toDTO(existingVisit);
        });
    }

}