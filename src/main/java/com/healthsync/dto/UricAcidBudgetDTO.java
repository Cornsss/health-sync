package com.healthsync.dto;

import java.time.LocalDate;

public class UricAcidBudgetDTO {
    private LocalDate date;
    private double totalBudgetMg;
    private double consumedMg;
    private double remainingMg;
    private double consumptionRate;
    private BudgetStatus status;
    private boolean fructoseWarning;
    private double fructoseTotalG;
    private int progressPercent;

    private UricAcidBudgetDTO() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final UricAcidBudgetDTO o = new UricAcidBudgetDTO();
        public Builder date(LocalDate v)        { o.date = v; return this; }
        public Builder totalBudgetMg(double v)  { o.totalBudgetMg = v; return this; }
        public Builder consumedMg(double v)     { o.consumedMg = v; return this; }
        public Builder remainingMg(double v)    { o.remainingMg = v; return this; }
        public Builder consumptionRate(double v){ o.consumptionRate = v; return this; }
        public Builder progressPercent(int v)   { o.progressPercent = v; return this; }
        public Builder status(BudgetStatus v)   { o.status = v; return this; }
        public Builder fructoseWarning(boolean v){ o.fructoseWarning = v; return this; }
        public Builder fructoseTotalG(double v) { o.fructoseTotalG = v; return this; }
        public UricAcidBudgetDTO build()        { return o; }
    }

    public LocalDate getDate() { return date; }
    public double getTotalBudgetMg() { return totalBudgetMg; }
    public double getConsumedMg() { return consumedMg; }
    public double getRemainingMg() { return remainingMg; }
    public double getConsumptionRate() { return consumptionRate; }
    public BudgetStatus getStatus() { return status; }
    public boolean isFructoseWarning() { return fructoseWarning; }
    public double getFructoseTotalG() { return fructoseTotalG; }
    public int getProgressPercent() { return progressPercent; }
}
