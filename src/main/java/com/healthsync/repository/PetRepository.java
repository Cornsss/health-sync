package com.healthsync.repository;

import com.healthsync.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    @Query("SELECT p FROM Pet p ORDER BY p.id ASC LIMIT 1")
    Optional<Pet> findFirstPet();
}