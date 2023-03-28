package com.PetClinic.mapper;

import com.PetClinic.entity.Pet;
import com.PetClinic.model.PetDTO;
import org.mapstruct.Mapper;

@Mapper
public interface PetMapper {

    Pet petDtoToPet(PetDTO dto);
    PetDTO petToPetDto(Pet pet);
}