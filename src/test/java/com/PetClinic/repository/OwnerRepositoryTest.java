//package com.PetClinic.repository;
//
//import com.PetClinic.entity.Owner;
//import jakarta.validation.ConstraintViolationException;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class OwnerRepositoryTest {
//
//    @Autowired
//    OwnerRepository ownerRepository;
//
//    @Test
//    void testSaveOwner() {
//        Owner testOwner = ownerRepository.save(Owner.builder()
//                .name("Owner name")
//                .telephone("23458")
//                .address("Bordeaux")
//                .build());
//        ownerRepository.flush();
//
//        assertThat(testOwner).isNotNull();
//        assertThat(testOwner.getId()).isNotNull();
//    }
//
//    @Test
//    void testSaveOwnerNameTooLong() {
//        assertThrows(ConstraintViolationException.class, () -> {
//            Owner testOwner = ownerRepository.save(Owner.builder()
//                    .name("Owner name 123456789123456789123456789123456789123456789123456789123456789123456789")
//                    .build());
//            ownerRepository.flush();
//        });
//    }
//
//}