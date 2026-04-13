package com.healthsync.service;

import com.healthsync.dto.FattyLiverProgressDTO;
import com.healthsync.entity.HealthMetrics;
import com.healthsync.entity.WaterLog;
import com.healthsync.repository.ExerciseLogRepository;
import com.healthsync.repository.HealthMetricsRepository;
import com.healthsync.repository.WaterLogRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FattyLiverProgressService {

    private static final double GGT_NORMAL  = 45.0;
    private static final double ALT_NORMAL  = 40.0;
    private static final int    FAT_BURN_TARGET = 150;

    private final HealthMetricsRepository metricsRepo;
    private final ExerciseLogRepository   exerciseRepo;
    private final WaterLogRepository      waterRepo;

    public FattyLiverProgressService(HealthMetricsRepository metricsRepo,
                                      ExerciseLogRepository exerciseRepo,
                                      WaterLogRepository waterRepo) {
        this.metricsRepo  = metricsRepo;
        this.exerciseRepo = exerciseRepo;
        this.waterRepo    = waterRepo;
    }

    public FattyLiverProgressDTO assessProgress() {
        HealthMetrics latest = metricsRepo.findFirstByOrderByRecordedAtDesc().orElse(null);
        if (latest == null) return FattyLiverProgressDTO.builder().hasData(false).build();

        LocalDate today         = LocalDate.now();
        LocalDate thirtyDaysAgo = today.minusDays(30);
        HealthMetrics baseline  = metricsRepo
                .findFirstByRecordedAtBeforeOrderByRecordedAtDesc(thirtyDaysAgo)
                .orElse(latest);

        double ggtScore  = scoreImprovement(toD(baseline.getGgt()), toD(latest.getGgt()), GGT_NORMAL, 20);
        double altScore  = scoreImprovement(toD(baseline.getAlt()), toD(latest.getAlt()), ALT_NORMAL, 20);

        double bodyFatScore = 0;
        if (latest.getBodyFatPct() != null) {
            double cur = latest.getBodyFatPct().doubleValue();
            double bas = baseline.getBodyFatPct() != null ? baseline.getBodyFatPct().doubleValue() : cur;
            bodyFatScore = Math.min(Math.max((bas - cur) * 5, 0), 30);
        }

        Integer fb = exerciseRepo.sumFatBurnMinutesBetween(today.minusDays(7), today);
        int weeklyFatBurnMin = fb != null ? fb : 0;
        double exerciseScore = Math.min((double) weeklyFatBurnMin / FAT_BURN_TARGET * 20, 20);

        double avgWater  = avgDailyWater(thirtyDaysAgo, today);
        double waterScore = Math.min(avgWater / 2500.0 * 10, 10);

        double total = ggtScore + altScore + bodyFatScore + exerciseScore + waterScore;

        return FattyLiverProgressDTO.builder()
                .hasData(true)
                .totalScore(Math.round(total * 10.0) / 10.0)
                .grade(resolveGrade(total))
                .gradeCss(resolveGradeCss(total))
                .ggtCurrent(toD(latest.getGgt()))
                .altCurrent(toD(latest.getAlt()))
                .bodyFatCurrent(latest.getBodyFatPct() != null ? latest.getBodyFatPct().doubleValue() : null)
                .weightCurrent(latest.getWeightKg() != null ? latest.getWeightKg().doubleValue() : null)
                .weeklyFatBurnMin(weeklyFatBurnMin)
                .avgDailyWaterMl(Math.round(avgWater))
                .ggtImproved(toD(latest.getGgt()) < toD(baseline.getGgt()))
                .altImproved(toD(latest.getAlt()) < toD(baseline.getAlt()))
                .estimatedRecoveryWeeks((int) Math.ceil((100 - total) / 100 * 24))
                .build();
    }

    private double scoreImprovement(double baseline, double current, double normal, double max) {
        if (current <= normal) return max;
        double be = Math.max(baseline - normal, 0);
        double ce = Math.max(current  - normal, 0);
        if (be == 0) return max;
        return Math.max((1 - ce / be) * max, 0);
    }

    private double avgDailyWater(LocalDate start, LocalDate end) {
        List<WaterLog> logs = waterRepo.findByLoggedDateBetween(start, end);
        Map<LocalDate, Integer> daily = logs.stream()
                .collect(Collectors.groupingBy(WaterLog::getLoggedDate,
                        Collectors.summingInt(WaterLog::getAmountMl)));
        return daily.values().stream().mapToInt(Integer::intValue).average().orElse(0);
    }

    private double toD(BigDecimal v) { return v != null ? v.doubleValue() : 0.0; }

    private String resolveGrade(double s) {
        if (s >= 85) return "优秀 — 修复显著";
        if (s >= 70) return "良好 — 稳步改善";
        if (s >= 50) return "一般 — 需加强干预";
        return "待改善 — 建议就医复查";
    }

    private String resolveGradeCss(double s) {
        if (s >= 85) return "success";
        if (s >= 70) return "primary";
        if (s >= 50) return "warning";
        return "danger";
    }
}
