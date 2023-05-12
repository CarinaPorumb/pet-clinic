package com.PetClinic.startup;

import com.PetClinic.entity.Owner;
import com.PetClinic.entity.Pet;
import com.PetClinic.entity.Vet;
import com.PetClinic.entity.Visit;
import com.PetClinic.model.PetCSVRecord;
import com.PetClinic.model.enums.PetType;
import com.PetClinic.model.enums.Speciality;
import com.PetClinic.repository.OwnerRepository;
import com.PetClinic.repository.PetRepository;
import com.PetClinic.repository.VetRepository;
import com.PetClinic.repository.VisitRepository;
import com.PetClinic.service.PetServiceCSV;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Component
public class StartupData implements CommandLineRunner {

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;
    private final PetServiceCSV petServiceCSV;
    private final VetRepository vetRepository;
    private final VisitRepository visitRepository;

    public StartupData(OwnerRepository ownerRepository, PetRepository petRepository, PetServiceCSV petServiceCSV, VetRepository vetRepository, VisitRepository visitRepository) {
        this.ownerRepository = ownerRepository;
        this.petRepository = petRepository;
        this.petServiceCSV = petServiceCSV;
        this.vetRepository = vetRepository;
        this.visitRepository = visitRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadOwnerData();
        loadVetAndVisitData();
        loadCsvData();
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
    }

    private void loadVetAndVisitData() {
        Vet vet1 = Vet.builder()
                .name("Vet1")
                .speciality(Speciality.NEUROLOGY)
                .build();

        Vet vet2 = Vet.builder()
                .name("Vet2")
                .speciality(Speciality.CARDIOLOGY)
                .build();

        Vet vet3 = Vet.builder()
                .name("Vet3")
                .speciality(Speciality.BEHAVIOR)
                .build();

        vetRepository.save(vet1);
        vetRepository.save(vet2);
        vetRepository.save(vet3);

        Visit visit1 = Visit.builder()
                .date(LocalDate.now())
                .price(10)
                .diagnosis("Cold")
                .build();

        Visit visit2 = Visit.builder()
                .date(LocalDate.now())
                .price(20)
                .diagnosis("Cold")
                .build();

        visitRepository.save(visit1);
        visitRepository.save(visit2);
    }

    private void loadCsvData() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/pet.csv");
        List<PetCSVRecord> records = petServiceCSV.convertCSV(file);

        records.forEach(petCSVRecord -> {
            PetType petType = switch (petCSVRecord.getPetType().toLowerCase()) {
                case "bird" -> PetType.BIRD;
                case "cat" -> PetType.CAT;
                case "dog" -> PetType.DOG;
                case "fish" -> PetType.FISH;
                default -> PetType.NOT_YET_DEFINED;
            };
            petRepository.save(Pet.builder()
                    .name(petCSVRecord.getName())
                    .petType(petType)
                    .age(petCSVRecord.getAge())
                    .weight(petCSVRecord.getWeight())
                    .build());
        });
    }

}