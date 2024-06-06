package com.PetClinic.mapper;

import com.PetClinic.entity.Pet;
import com.PetClinic.model.PetDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetMapper {

    @Mapping(target = "owner", ignore = true)
    Pet toEntity(PetDTO dto);

    @Mapping(target = "ownerId", source = "owner.id")
    PetDTO toDTO(Pet pet);

}