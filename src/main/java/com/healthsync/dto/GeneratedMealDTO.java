package com.healthsync.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GeneratedMealDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, PlanInfo> plans = new LinkedHashMap<>();
    private double totalPurineMg;
    private double totalCalories;
    private double totalFructoseG;
    private Map<String, Double> shoppingList = new LinkedHashMap<>();

    public GeneratedMealDTO() {}

    public Map<String, PlanInfo> getPlans() { return plans; }
    public void setPlans(Map<String, PlanInfo> plans) { this.plans = plans; }
    public double getTotalPurineMg() { return totalPurineMg; }
    public void setTotalPurineMg(double totalPurineMg) { this.totalPurineMg = totalPurineMg; }
    public double getTotalCalories() { return totalCalories; }
    public void setTotalCalories(double totalCalories) { this.totalCalories = totalCalories; }
    public double getTotalFructoseG() { return totalFructoseG; }
    public void setTotalFructoseG(double totalFructoseG) { this.totalFructoseG = totalFructoseG; }
    public Map<String, Double> getShoppingList() { return shoppingList; }
    public void setShoppingList(Map<String, Double> shoppingList) { this.shoppingList = shoppingList; }

    public static class PlanInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        private String planName;
        private String description;
        private List<ItemInfo> items = new ArrayList<>();

        public String getPlanName() { return planName; }
        public void setPlanName(String planName) { this.planName = planName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public List<ItemInfo> getItems() { return items; }
        public void setItems(List<ItemInfo> items) { this.items = items; }
    }

    public static class ItemInfo implements Serializable {
        private static final long serialVersionUID = 1L;
        private String foodName;
        private double amountG;
        private String cookingMethod;

        public String getFoodName() { return foodName; }
        public void setFoodName(String foodName) { this.foodName = foodName; }
        public double getAmountG() { return amountG; }
        public void setAmountG(double amountG) { this.amountG = amountG; }
        public String getCookingMethod() { return cookingMethod; }
        public void setCookingMethod(String cookingMethod) { this.cookingMethod = cookingMethod; }
    }
}