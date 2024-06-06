//package com.PetClinic.startup;
//
//import com.PetClinic.repository.OwnerRepository;
//import com.PetClinic.repository.PetRepository;
//import com.PetClinic.repository.VetRepository;
//import com.PetClinic.repository.VisitRepository;
//import com.PetClinic.service.impl.CSVConversionServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@DataJpaTest
//@Import(CSVConversionServiceImpl.class)
//class StartupDataTest {
//
//    @Autowired
//    PetRepository petRepository;
//    @Autowired
//    OwnerRepository ownerRepository;
//    @Autowired
//    VetRepository vetRepository;
//    @Autowired
//    VisitRepository visitRepository;
//    @Autowired
//    CSVConversionServiceImpl petServiceCSV;
//    StartupData startupData;
//
//    @BeforeEach
//    void setUp() {
//        startupData = new StartupData(ownerRepository, petRepository, petServiceCSV, vetRepository, visitRepository);
//    }
//
//    @Test
//    void run() throws Exception {
//        startupData.run((String) null);
//        assertThat(ownerRepository.count()).isEqualTo(3);
//        assertThat(vetRepository.count()).isEqualTo(3);
//        assertThat(visitRepository.count()).isEqualTo(2);
//        assertThat(petRepository.count()).isEqualTo(74);
//    }
//}