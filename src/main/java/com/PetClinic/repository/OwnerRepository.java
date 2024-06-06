package com.PetClinic.repository;

import com.PetClinic.entity.Owner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OwnerRepository extends JpaRepository<Owner, UUID> {

    @Query("SELECT o FROM Owner o WHERE o.name LIKE %:name%")
    List<Owner> searchByNameLike(String name);

    Page<Owner> findByNameContainingIgnoreCase(String name, Pageable pageable);
}