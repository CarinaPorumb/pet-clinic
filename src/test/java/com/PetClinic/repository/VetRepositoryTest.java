//package com.PetClinic.repository;
//
//import com.PetClinic.entity.Pet;
//import com.PetClinic.entity.Vet;
//import com.PetClinic.service.impl.CSVConversionServiceImpl;
//import com.PetClinic.startup.StartupData;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.transaction.annotation.Transactional;
//
//@DataJpaTest
//@Import({StartupData.class, CSVConversionServiceImpl.class})
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class VetRepositoryTest {
//
//    @Autowired
//    VetRepository vetRepository;
//
//    @Autowired
//    PetRepository petRepository;
//
//    Pet testPet;
//
//    @BeforeEach
//    void setUp() {
//        testPet = petRepository.findAll().get(1);
//    }
//
//    @Transactional
//    @Test
//    void testAddVet() {
//        Vet savedVet = vetRepository.save(Vet.builder()
//                .name("Test Vet")
//                .build());
//
//        testPet.addVet(savedVet);
//        Pet savePet = petRepository.save(testPet);
//
//        System.out.println(savePet.getName());
//    }
//
//}