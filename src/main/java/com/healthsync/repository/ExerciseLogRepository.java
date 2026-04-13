package com.healthsync.repository;

import com.healthsync.entity.ExerciseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {

    List<ExerciseLog> findByExerciseDateOrderByCreatedAtDesc(LocalDate date);

    List<ExerciseLog> findByExerciseDateBetweenOrderByExerciseDateAsc(LocalDate start, LocalDate end);

    @Query("SELECT SUM(e.fatBurnMin) FROM ExerciseLog e WHERE e.exerciseDate BETWEEN :start AND :end")
    Integer sumFatBurnMinutesBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);
}
