package com.PetClinic.mapper;

import com.PetClinic.entity.Owner;
import com.PetClinic.entity.Pet;
import com.PetClinic.model.OwnerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OwnerMapper {

    @Mapping(target = "pets", ignore = true)
    Owner toEntity(OwnerDTO dto);

    @Mapping(target = "petIds", source = "pets", qualifiedByName = "petToPetId")
    OwnerDTO toDTO(Owner owner);

    @Named("petToPetId")
    default UUID petToPetId(Pet pet) {
        return pet.getId();
    }

}