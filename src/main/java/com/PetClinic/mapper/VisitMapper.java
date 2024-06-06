package com.PetClinic.mapper;

import com.PetClinic.entity.Visit;
import com.PetClinic.model.VisitDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VisitMapper {
    Visit toEntity(VisitDTO dto);
    VisitDTO toDTO(Visit visit);
}