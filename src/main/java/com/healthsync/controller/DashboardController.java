package com.healthsync.controller;

import com.healthsync.dto.DashboardDTO;
import com.healthsync.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
public class DashboardController {

    private final UricAcidBudgetService     budgetService;
    private final FattyLiverProgressService progressService;
    private final WaterTrackingService      waterService;
    private final HealthMetricsService      metricsService;
    private final ExerciseService           exerciseService;

    public DashboardController(UricAcidBudgetService budgetService,
                                FattyLiverProgressService progressService,
                                WaterTrackingService waterService,
                                HealthMetricsService metricsService,
                                ExerciseService exerciseService) {
        this.budgetService   = budgetService;
        this.progressService = progressService;
        this.waterService    = waterService;
        this.metricsService  = metricsService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        int water = waterService.getTodayTotal();
        DashboardDTO dto = DashboardDTO.builder()
                .uricAcidBudget(budgetService.calculateDailyBudget(LocalDate.now()))
                .fattyLiverProgress(progressService.assessProgress())
                .todayWaterMl(water)
                .waterGoalMl(WaterTrackingService.DAILY_GOAL_ML)
                .waterProgressPct(waterService.getProgressPercent(water))
                .latestMetrics(metricsService.findLatest().orElse(null))
                .recentExercise(exerciseService.getWeeklyLogs())
                .weeklyFatBurnMin(exerciseService.getWeeklyFatBurnMin())
                .todayDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日")))
                .build();
        model.addAttribute("d", dto);
        model.addAttribute("currentPage", "dashboard");
        return "dashboard";
    }
}
