package com.healthsync.repository;

import com.healthsync.entity.MealRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealRecordRepository extends JpaRepository<MealRecord, Long> {

    List<MealRecord> findByRecordedDateOrderByCreatedAtAsc(LocalDate date);

    @Query("SELECT SUM(m.totalPurineMg) FROM MealRecord m WHERE m.recordedDate = :date")
    Optional<Double> sumPurineByDate(@Param("date") LocalDate date);

    @Query("SELECT SUM(m.totalFructoseG) FROM MealRecord m WHERE m.recordedDate = :date")
    Optional<Double> sumFructoseByDate(@Param("date") LocalDate date);

    void deleteByRecordedDate(LocalDate date);
}
