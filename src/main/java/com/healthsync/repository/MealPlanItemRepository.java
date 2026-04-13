package com.healthsync.repository;

import com.healthsync.entity.MealPlanItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealPlanItemRepository extends JpaRepository<MealPlanItem, Long> {

    List<MealPlanItem> findByPlanId(Long planId);
}
