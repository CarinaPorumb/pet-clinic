package com.PetClinic.mapper;

import com.PetClinic.entity.Visit;
import com.PetClinic.model.VisitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VisitMapper {

    Visit toEntity(VisitDTO dto);

    @Mapping(target = "pet.visits", ignore = true)
    VisitDTO toDTO(Visit visit);
}