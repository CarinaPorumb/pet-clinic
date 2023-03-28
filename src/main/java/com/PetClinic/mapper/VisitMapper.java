package com.PetClinic.mapper;

import com.PetClinic.entity.Visit;
import com.PetClinic.model.VisitDTO;
import org.mapstruct.Mapper;

@Mapper
public interface VisitMapper {

    Visit visitDtoToVisit(VisitDTO dto);
    VisitDTO visitToVisitDto(Visit visit);
}