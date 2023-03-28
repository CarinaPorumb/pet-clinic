package com.PetClinic.mapper;

import com.PetClinic.entity.Owner;
import com.PetClinic.model.OwnerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface OwnerMapper {

    Owner ownerDtoToOwner(OwnerDTO dto);
    OwnerDTO ownerToOwnerDto(Owner owner);
}