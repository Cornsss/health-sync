package com.healthsync.repository;

import com.healthsync.entity.FoodLibrary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodLibraryRepository extends JpaRepository<FoodLibrary, Long> {

    List<FoodLibrary> findByAllowedForUricAcidTrue();

    List<FoodLibrary> findByCategory(String category);

    Optional<FoodLibrary> findByName(String name);
}
