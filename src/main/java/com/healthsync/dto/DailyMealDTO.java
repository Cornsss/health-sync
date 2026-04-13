package com.healthsync.dto;

import com.healthsync.entity.MealPlan;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class DailyMealDTO {
    private LocalDate date;
    private Map<String, MealPlan> plans = new LinkedHashMap<>();
    private double totalPurineMg;
    private double totalCalories;
    private double totalFructoseG;
    private Map<String, Double> shoppingList = new LinkedHashMap<>();

    private DailyMealDTO() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final DailyMealDTO o = new DailyMealDTO();
        public Builder date(LocalDate v)                     { o.date = v; return this; }
        public Builder plans(Map<String, MealPlan> v)        { o.plans = v; return this; }
        public Builder totalPurineMg(double v)               { o.totalPurineMg = v; return this; }
        public Builder totalCalories(double v)               { o.totalCalories = v; return this; }
        public Builder totalFructoseG(double v)              { o.totalFructoseG = v; return this; }
        public Builder shoppingList(Map<String, Double> v)   { o.shoppingList = v; return this; }
        public DailyMealDTO build()                          { return o; }
    }

    public LocalDate getDate() { return date; }
    public Map<String, MealPlan> getPlans() { return plans; }
    public double getTotalPurineMg() { return totalPurineMg; }
    public double getTotalCalories() { return totalCalories; }
    public double getTotalFructoseG() { return totalFructoseG; }
    public Map<String, Double> getShoppingList() { return shoppingList; }
}
