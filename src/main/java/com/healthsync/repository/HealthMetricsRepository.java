package com.healthsync.repository;

import com.healthsync.entity.HealthMetrics;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HealthMetricsRepository extends JpaRepository<HealthMetrics, Long> {

    Optional<HealthMetrics> findFirstByOrderByRecordedAtDesc();

    Optional<HealthMetrics> findFirstByUricAcidIsNotNullOrderByRecordedAtDesc();

    Optional<HealthMetrics> findFirstByRecordedAtBeforeOrderByRecordedAtDesc(LocalDate date);

    List<HealthMetrics> findByRecordedAtBetweenOrderByRecordedAtAsc(LocalDate start, LocalDate end);

    @Query("SELECT m FROM HealthMetrics m ORDER BY m.recordedAt DESC")
    List<HealthMetrics> findRecentPaged(Pageable pageable);
}
