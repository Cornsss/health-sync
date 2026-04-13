package com.healthsync.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "meal_records")
public class MealRecord {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate recordedDate = LocalDate.now();

    @Column(nullable = false, length = 20)
    private String mealType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private MealPlan plan;

    private BigDecimal totalPurineMg;
    private BigDecimal totalFructoseG;
    private BigDecimal totalCalories;
    private Boolean isRandomGenerated = false;
    private LocalDateTime createdAt = LocalDateTime.now();

    public MealRecord() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getRecordedDate() { return recordedDate; }
    public void setRecordedDate(LocalDate recordedDate) { this.recordedDate = recordedDate; }
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    public MealPlan getPlan() { return plan; }
    public void setPlan(MealPlan plan) { this.plan = plan; }
    public BigDecimal getTotalPurineMg() { return totalPurineMg; }
    public void setTotalPurineMg(BigDecimal totalPurineMg) { this.totalPurineMg = totalPurineMg; }
    public BigDecimal getTotalFructoseG() { return totalFructoseG; }
    public void setTotalFructoseG(BigDecimal totalFructoseG) { this.totalFructoseG = totalFructoseG; }
    public BigDecimal getTotalCalories() { return totalCalories; }
    public void setTotalCalories(BigDecimal totalCalories) { this.totalCalories = totalCalories; }
    public Boolean getIsRandomGenerated() { return isRandomGenerated; }
    public void setIsRandomGenerated(Boolean isRandomGenerated) { this.isRandomGenerated = isRandomGenerated; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
