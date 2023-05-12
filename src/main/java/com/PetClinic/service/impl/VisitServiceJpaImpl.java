package com.PetClinic.service.impl;

import com.PetClinic.mapper.VisitMapper;
import com.PetClinic.model.VisitDTO;
import com.PetClinic.repository.VisitRepository;
import com.PetClinic.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
@Primary
@RequiredArgsConstructor
@Service
public class VisitServiceJpaImpl implements VisitService {

    private final VisitRepository visitRepository;
    private final VisitMapper visitMapper;

    @Override
    public List<VisitDTO> listVisits() {
        return visitRepository.findAll()
                .stream()
                .map(visitMapper::visitToVisitDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<VisitDTO> getById(UUID id) {
        return Optional.ofNullable(visitMapper.visitToVisitDto(visitRepository.findById(id).orElse(null)));
    }

    @Override
    public VisitDTO saveNewVisit(VisitDTO visitDTO) {
        return visitMapper.visitToVisitDto(visitRepository.save(visitMapper.visitDtoToVisit(visitDTO)));
    }

    @Override
    public Optional<VisitDTO> updateVisit(UUID id, VisitDTO visitDTO) {
        AtomicReference<Optional<VisitDTO>> atomicReference = new AtomicReference<>();
        visitRepository.findById(id).ifPresentOrElse(foundVisit -> {
            foundVisit.setDate(visitDTO.getDate());
            foundVisit.setDiagnosis(visitDTO.getDiagnosis());
            foundVisit.setPrice(visitDTO.getPrice());
            atomicReference.set(Optional.of(visitMapper.visitToVisitDto(visitRepository.save(foundVisit))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public boolean deleteById(UUID id) {
        if (visitRepository.existsById(id)) {
            visitRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<VisitDTO> patchById(UUID id, VisitDTO visitDTO) {
        AtomicReference<Optional<VisitDTO>> atomicReference = new AtomicReference<>();
        visitRepository.findById(id).ifPresentOrElse(foundVisit -> {
            if (StringUtils.hasText(visitDTO.getDiagnosis())) {
                foundVisit.setDiagnosis(visitDTO.getDiagnosis());
            }
            if (visitDTO.getDate() != null) {
                foundVisit.setDate(visitDTO.getDate());
            }
            if (visitDTO.getPrice() != null) {
                foundVisit.setPrice(visitDTO.getPrice());
            }
            atomicReference.set(Optional.of(visitMapper.visitToVisitDto(visitRepository.save(foundVisit))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }
}
