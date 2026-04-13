package com.healthsync.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meal_plans")
public class MealPlan {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String mealType;

    @Column(nullable = false, length = 100)
    private String planName;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Boolean isActive = true;
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "plan", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MealPlanItem> items = new ArrayList<>();

    public MealPlan() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<MealPlanItem> getItems() { return items; }
    public void setItems(List<MealPlanItem> items) { this.items = items; }
}
