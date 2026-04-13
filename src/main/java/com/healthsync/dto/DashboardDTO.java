package com.healthsync.dto;

import com.healthsync.entity.ExerciseLog;
import com.healthsync.entity.HealthMetrics;
import java.util.List;

public class DashboardDTO {
    private UricAcidBudgetDTO uricAcidBudget;
    private FattyLiverProgressDTO fattyLiverProgress;
    private int todayWaterMl;
    private int waterGoalMl;
    private int waterProgressPct;
    private HealthMetrics latestMetrics;
    private List<ExerciseLog> recentExercise;
    private int weeklyFatBurnMin;
    private String todayDate;

    private DashboardDTO() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final DashboardDTO o = new DashboardDTO();
        public Builder uricAcidBudget(UricAcidBudgetDTO v)         { o.uricAcidBudget = v; return this; }
        public Builder fattyLiverProgress(FattyLiverProgressDTO v)  { o.fattyLiverProgress = v; return this; }
        public Builder todayWaterMl(int v)                          { o.todayWaterMl = v; return this; }
        public Builder waterGoalMl(int v)                           { o.waterGoalMl = v; return this; }
        public Builder waterProgressPct(int v)                      { o.waterProgressPct = v; return this; }
        public Builder latestMetrics(HealthMetrics v)               { o.latestMetrics = v; return this; }
        public Builder recentExercise(List<ExerciseLog> v)          { o.recentExercise = v; return this; }
        public Builder weeklyFatBurnMin(int v)                      { o.weeklyFatBurnMin = v; return this; }
        public Builder todayDate(String v)                          { o.todayDate = v; return this; }
        public DashboardDTO build()                                 { return o; }
    }

    public UricAcidBudgetDTO getUricAcidBudget() { return uricAcidBudget; }
    public FattyLiverProgressDTO getFattyLiverProgress() { return fattyLiverProgress; }
    public int getTodayWaterMl() { return todayWaterMl; }
    public int getWaterGoalMl() { return waterGoalMl; }
    public int getWaterProgressPct() { return waterProgressPct; }
    public HealthMetrics getLatestMetrics() { return latestMetrics; }
    public List<ExerciseLog> getRecentExercise() { return recentExercise; }
    public int getWeeklyFatBurnMin() { return weeklyFatBurnMin; }
    public String getTodayDate() { return todayDate; }
}
