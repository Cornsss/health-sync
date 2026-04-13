package com.healthsync.dto;

public class FattyLiverProgressDTO {
    private boolean hasData;
    private double totalScore;
    private String grade;
    private String gradeCss;
    private Double ggtCurrent;
    private Double altCurrent;
    private Double bodyFatCurrent;
    private Double weightCurrent;
    private int weeklyFatBurnMin;
    private double avgDailyWaterMl;
    private boolean ggtImproved;
    private boolean altImproved;
    private int estimatedRecoveryWeeks;

    private FattyLiverProgressDTO() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final FattyLiverProgressDTO o = new FattyLiverProgressDTO();
        public Builder hasData(boolean v)             { o.hasData = v; return this; }
        public Builder totalScore(double v)           { o.totalScore = v; return this; }
        public Builder grade(String v)                { o.grade = v; return this; }
        public Builder gradeCss(String v)             { o.gradeCss = v; return this; }
        public Builder ggtCurrent(Double v)           { o.ggtCurrent = v; return this; }
        public Builder altCurrent(Double v)           { o.altCurrent = v; return this; }
        public Builder bodyFatCurrent(Double v)       { o.bodyFatCurrent = v; return this; }
        public Builder weightCurrent(Double v)        { o.weightCurrent = v; return this; }
        public Builder weeklyFatBurnMin(int v)        { o.weeklyFatBurnMin = v; return this; }
        public Builder avgDailyWaterMl(double v)      { o.avgDailyWaterMl = v; return this; }
        public Builder ggtImproved(boolean v)         { o.ggtImproved = v; return this; }
        public Builder altImproved(boolean v)         { o.altImproved = v; return this; }
        public Builder estimatedRecoveryWeeks(int v)  { o.estimatedRecoveryWeeks = v; return this; }
        public FattyLiverProgressDTO build()          { return o; }
    }

    public boolean isHasData() { return hasData; }
    public double getTotalScore() { return totalScore; }
    public String getGrade() { return grade; }
    public String getGradeCss() { return gradeCss; }
    public Double getGgtCurrent() { return ggtCurrent; }
    public Double getAltCurrent() { return altCurrent; }
    public Double getBodyFatCurrent() { return bodyFatCurrent; }
    public Double getWeightCurrent() { return weightCurrent; }
    public int getWeeklyFatBurnMin() { return weeklyFatBurnMin; }
    public double getAvgDailyWaterMl() { return avgDailyWaterMl; }
    public boolean isGgtImproved() { return ggtImproved; }
    public boolean isAltImproved() { return altImproved; }
    public int getEstimatedRecoveryWeeks() { return estimatedRecoveryWeeks; }
}
