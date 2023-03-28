package com.PetClinic.mapper;

import com.PetClinic.entity.Person;
import com.PetClinic.model.PersonDTO;
import org.mapstruct.Mapper;

@Mapper
public interface PersonMapper {

    Person personDtoToPerson(PersonDTO dto);
    PersonDTO personToPersonDto(Person person);
}