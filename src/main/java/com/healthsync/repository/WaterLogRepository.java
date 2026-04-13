package com.healthsync.repository;

import com.healthsync.entity.WaterLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WaterLogRepository extends JpaRepository<WaterLog, Long> {

    List<WaterLog> findByLoggedDateOrderByLoggedAtDesc(LocalDate date);

    @Query("SELECT SUM(w.amountMl) FROM WaterLog w WHERE w.loggedDate = :date")
    Optional<Integer> sumByDate(@Param("date") LocalDate date);

    @Query("SELECT w.loggedDate, SUM(w.amountMl) FROM WaterLog w " +
           "WHERE w.loggedDate BETWEEN :start AND :end " +
           "GROUP BY w.loggedDate ORDER BY w.loggedDate")
    List<Object[]> findDailySumBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    Optional<WaterLog> findFirstByOrderByLoggedAtDesc();

    List<WaterLog> findByLoggedDateBetween(LocalDate start, LocalDate end);

    @Query("SELECT w FROM WaterLog w WHERE w.loggedDate = :date ORDER BY w.loggedAt ASC LIMIT 1")
    Optional<WaterLog> findFirstByDateOrderByTimeAsc(@Param("date") LocalDate date);

    @Query("SELECT COUNT(DISTINCT w.loggedDate) FROM WaterLog w WHERE w.loggedDate BETWEEN :start AND :end")
    long countDaysWithLogs(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
