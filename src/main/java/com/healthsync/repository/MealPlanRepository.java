package com.healthsync.repository;

import com.healthsync.entity.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {

    List<MealPlan> findByMealTypeAndIsActiveTrue(String mealType);

    List<MealPlan> findByIsActiveTrue();
}
