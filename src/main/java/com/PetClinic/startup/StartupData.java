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
import com.PetClinic.service.CSVConversionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class StartupData implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartupData.class);

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;
    private final CSVConversionService<PetCSVRecord> petCSVConversionService; // Specify
    private final VetRepository vetRepository;
    private final VisitRepository visitRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Starting data initialization...");

        loadOwnerData();
        loadVetAndVisitData();
        loadCsvData();
        LOGGER.info("Data initialization complete.");
    }

    private void loadOwnerData() {
        LOGGER.info("Loading owner data...");
        Owner owner1 = Owner.builder()
                .id(UUID.randomUUID())
                .name("John")
                .address("Paris")
                .telephone("01234")
                .build();

        Owner owner2 = Owner.builder()
                .id(UUID.randomUUID())
                .name("Helen")
                .address("London")
                .telephone("123456")
                .build();

        Owner owner3 = Owner.builder()
                .id(UUID.randomUUID())
                .name("Maria")
                .address("Lisbon")
                .telephone("23456")
                .build();

        owner1 = ownerRepository.saveAndFlush(owner1);
        owner2 = ownerRepository.saveAndFlush(owner2);
        owner3 = ownerRepository.saveAndFlush(owner3);

        LOGGER.info("Owners saved: {}, {}, {}", owner1.getId(), owner2.getId(), owner3.getId());

        Pet pet1 = Pet.builder()
                .id(UUID.randomUUID())
                .name("Fluffy")
                .petType(PetType.DOG)
                .age(2)
                .weight(5.0)
                .owner(owner1)
                .build();

        Pet pet2 = Pet.builder()
                .id(UUID.randomUUID())
                .name("Whiskers")
                .petType(PetType.CAT)
                .age(3)
                .weight(4.0)
                .owner(owner2)
                .build();

        petRepository.save(pet1);
        petRepository.save(pet2);

        LOGGER.info("Pets saved: {}, {}", pet1.getId(), pet2.getId());
    }

    private void loadVetAndVisitData() {
        LOGGER.info("Loading vet and visit data...");

        Vet vet1 = Vet.builder()
                .id(UUID.randomUUID())
                .name("Vet1")
                .speciality(Speciality.NEUROLOGY)
                .build();

        Vet vet2 = Vet.builder()
                .id(UUID.randomUUID())
                .name("Vet2")
                .speciality(Speciality.CARDIOLOGY)
                .build();

        Vet vet3 = Vet.builder()
                .id(UUID.randomUUID())
                .name("Vet3")
                .speciality(Speciality.BEHAVIOR)
                .build();

        vetRepository.save(vet1);
        vetRepository.save(vet2);
        vetRepository.save(vet3);

        LOGGER.info("Vets saved: {}, {}, {}", vet1.getId(), vet2.getId(), vet3.getId());


        List<Pet> pets = petRepository.findAll();

        if (!pets.isEmpty()) {
            Pet pet1 = pets.get(0);
            Pet pet2 = pets.get(1);

            Visit visit1 = Visit.builder()
                    .id(UUID.randomUUID())
                    .date(LocalDate.now())
                    .price(10)
                    .diagnosis("Cold")
                    .pet(pet1)
                    .build();

            Visit visit2 = Visit.builder()
                    .id(UUID.randomUUID())
                    .date(LocalDate.now())
                    .price(20)
                    .diagnosis("Cold")
                    .pet(pet2)
                    .build();

            visitRepository.save(visit1);
            visitRepository.save(visit2);

            LOGGER.info("Visits saved: {}, {}", visit1.getId(), visit2.getId());
        }
    }

    private void loadCsvData() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/pet.csv");
        List<PetCSVRecord> records = petCSVConversionService.convertCSV(file);

        List<Owner> owners = ownerRepository.findAll();
        if (owners.isEmpty()) {
            throw new IllegalStateException("No owners found in the database. Cannot load CSV data.");
        }

        Owner defaultOwner = owners.getFirst();

        LOGGER.info("Default owner selected: {}", defaultOwner.getId());

        records.forEach(petCSVRecord -> {
                    PetType petType = switch (petCSVRecord.getPetType().toLowerCase()) {
                        case "bird" -> PetType.BIRD;
                        case "cat" -> PetType.CAT;
                        case "dog" -> PetType.DOG;
                        case "fish" -> PetType.FISH;
                        default -> PetType.NOT_YET_DEFINED;
                    };

                    Pet pet = Pet.builder()
                            .id(UUID.randomUUID())
                            .name(petCSVRecord.getName())
                            .petType(petType)
                            .age(petCSVRecord.getAge())
                            .weight(petCSVRecord.getWeight())
                            .owner(defaultOwner)
                            .build();

                    petRepository.save(pet);

                    LOGGER.info("CSV Pet saved: {}, Owner: {}", pet.getId(), defaultOwner.getId());
                }
        );
    }
}