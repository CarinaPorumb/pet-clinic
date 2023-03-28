package com.PetClinic.mapper;

import com.PetClinic.entity.Vet;
import com.PetClinic.model.VetDTO;
import org.mapstruct.Mapper;

@Mapper
public interface VetMapper {

    Vet vetDtoToVet(VetDTO dto);
    VetDTO vetToVetDto(Vet vet);
}