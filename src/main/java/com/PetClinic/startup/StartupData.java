package com.PetClinic.startup;

import com.PetClinic.entity.Owner;
import com.PetClinic.entity.Pet;
import com.PetClinic.entity.Vet;
import com.PetClinic.entity.Visit;
import com.PetClinic.model.enums.PetType;
import com.PetClinic.model.enums.Speciality;
import com.PetClinic.repository.OwnerRepository;
import com.PetClinic.repository.PetRepository;
import com.PetClinic.repository.VetRepository;
import com.PetClinic.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class StartupData implements CommandLineRunner {

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;
    private final VetRepository vetRepository;
    private final VisitRepository visitRepository;

    @Override
    public void run(String... args) throws Exception {
        loadOwnerData();

    }

    private void loadOwnerData() {
        Owner owner1 = Owner.builder()
                .name("John")
                .address("Paris")
                .telephone("01234")
                .build();

        Owner owner2 = Owner.builder()
                .name("Helen")
                .address("London")
                .telephone("123456")
                .build();

        Owner owner3 = Owner.builder()
                .name("Maria")
                .address("Lisbon")
                .telephone("23456")
                .build();

        ownerRepository.save(owner1);
        ownerRepository.save(owner2);
        ownerRepository.save(owner3);

        Vet vet1 = Vet.builder()
                .name("Vet1")
                .speciality(Speciality.NEUROLOGY)
                .build();

        Vet vet2 = Vet.builder()
                .name("Vet1")
                .speciality(Speciality.CARDIOLOGY)
                .build();

        Vet vet3 = Vet.builder()
                .name("Vet1")
                .speciality(Speciality.BEHAVIOR)
                .build();

        vetRepository.save(vet1);
        vetRepository.save(vet2);
        vetRepository.save(vet3);

        Pet dog = Pet.builder()
                .name("Jarrus")
                .petType(PetType.DOG)
                .birthDate(LocalDate.now())
                .build();

        Pet cat = Pet.builder()
                .name("Rocco")
                .petType(PetType.CAT)
                .birthDate(LocalDate.now())
                .build();

        petRepository.save(dog);
        petRepository.save(cat);

        Visit visit1 = Visit.builder()
                .date(LocalDate.now())
                .price(10)
                .diagnosis("Cold")
                .build();

        Visit visit2 = Visit.builder()
                .date(LocalDate.now())
                .price(10)
                .diagnosis("Cold")
                .build();

        visitRepository.save(visit1);
        visitRepository.save(visit2);

    }
}


