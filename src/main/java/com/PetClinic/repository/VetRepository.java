package com.PetClinic.repository;

import com.PetClinic.entity.Vet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface VetRepository extends JpaRepository<Vet, UUID>, JpaSpecificationExecutor<Vet> {

}