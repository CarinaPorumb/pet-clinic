package com.PetClinic.repository;

import com.PetClinic.entity.Pet;
import com.PetClinic.model.enums.PetType;
import com.PetClinic.service.impl.PetServiceCSVImpl;
import com.PetClinic.startup.StartupData;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({StartupData.class, PetServiceCSVImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PetRepositoryTest {

    @Autowired
    PetRepository petRepository;

    @Test
    void testSavePet() {
        Pet savedPet = petRepository.save(Pet.builder()
                .name("TestPet")
                .petType(PetType.BIRD)
                .age(10)
                .weight(0.3)
                .build());
        petRepository.flush();
        assertThat(savedPet).isNotNull();
        assertThat(savedPet.getId()).isNotNull();
    }


  @Test
  void testSavePetNameTooLong(){
        assertThrows(ConstraintViolationException.class, () ->{
            Pet savedPet = petRepository.save(Pet.builder()
                            .name("Pet 123456789123456789123456789123456789123456789123456789123456789123456789")
                            .petType(PetType.CAT)
                            .age(10)
                            .weight(5.3)
                    .build());
            petRepository.flush();

        });
  }

    @Test
    void findAllByNameIsLikeIgnoreCase() {
        Page<Pet> list = petRepository.findAllByNameIsLikeIgnoreCase("%Duchess%", null);
        assertThat(list.getContent().size()).isGreaterThan(0);
    }

}