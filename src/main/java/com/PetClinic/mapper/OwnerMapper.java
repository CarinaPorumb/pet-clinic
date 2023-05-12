package com.PetClinic.mapper;

import com.PetClinic.entity.Owner;
import org.mapstruct.Mapper;

@Mapper
public interface OwnerMapper {

    Owner ownerDtoToOwner(com.PetClinic.model.OwnerDTO dto);
    com.PetClinic.model.OwnerDTO ownerToOwnerDto(Owner owner);
}