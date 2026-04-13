package com.healthsync.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "exercise_logs")
public class ExerciseLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate exerciseDate = LocalDate.now();

    @Column(length = 50)
    private String exerciseType;

    @Column(nullable = false)
    private Integer durationMin;

    private Integer avgHeartRate;
    private Integer maxHeartRate;
    private Integer fatBurnMin;
    private BigDecimal caloriesBurned;

    @Column(columnDefinition = "TEXT")
    private String note;

    private LocalDateTime createdAt = LocalDateTime.now();

    public ExerciseLog() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getExerciseDate() { return exerciseDate; }
    public void setExerciseDate(LocalDate exerciseDate) { this.exerciseDate = exerciseDate; }
    public String getExerciseType() { return exerciseType; }
    public void setExerciseType(String exerciseType) { this.exerciseType = exerciseType; }
    public Integer getDurationMin() { return durationMin; }
    public void setDurationMin(Integer durationMin) { this.durationMin = durationMin; }
    public Integer getAvgHeartRate() { return avgHeartRate; }
    public void setAvgHeartRate(Integer avgHeartRate) { this.avgHeartRate = avgHeartRate; }
    public Integer getMaxHeartRate() { return maxHeartRate; }
    public void setMaxHeartRate(Integer maxHeartRate) { this.maxHeartRate = maxHeartRate; }
    public Integer getFatBurnMin() { return fatBurnMin; }
    public void setFatBurnMin(Integer fatBurnMin) { this.fatBurnMin = fatBurnMin; }
    public BigDecimal getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(BigDecimal caloriesBurned) { this.caloriesBurned = caloriesBurned; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
