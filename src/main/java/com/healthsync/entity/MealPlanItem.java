package com.healthsync.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "meal_plan_items")
public class MealPlanItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private MealPlan plan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "food_id", nullable = false)
    private FoodLibrary food;

    @Column(nullable = false)
    private BigDecimal amountG;

    @Column(columnDefinition = "TEXT")
    private String cookingMethod;

    public MealPlanItem() {}

    @Transient
    public double getPurineMg() {
        return food.getPurinePerHundredG().doubleValue() * amountG.doubleValue() / 100.0;
    }

    @Transient
    public double getFructoseG() {
        return food.getFructosePerHundredG().doubleValue() * amountG.doubleValue() / 100.0;
    }

    @Transient
    public double getCalories() {
        return food.getCaloriesPerHundredG().doubleValue() * amountG.doubleValue() / 100.0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public MealPlan getPlan() { return plan; }
    public void setPlan(MealPlan plan) { this.plan = plan; }
    public FoodLibrary getFood() { return food; }
    public void setFood(FoodLibrary food) { this.food = food; }
    public BigDecimal getAmountG() { return amountG; }
    public void setAmountG(BigDecimal amountG) { this.amountG = amountG; }
    public String getCookingMethod() { return cookingMethod; }
    public void setCookingMethod(String cookingMethod) { this.cookingMethod = cookingMethod; }
}
