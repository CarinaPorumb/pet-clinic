package com.PetClinic.mapper;

import com.PetClinic.entity.Vet;
import com.PetClinic.model.VetDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VetMapper {

    Vet toEntity(VetDTO dto);
    VetDTO toDTO(Vet vet);
}