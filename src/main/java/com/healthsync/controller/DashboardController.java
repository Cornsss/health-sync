package com.healthsync.controller;

import com.healthsync.dto.DashboardDTO;
import com.healthsync.entity.Pet;
import com.healthsync.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
public class DashboardController {

    private final UricAcidBudgetService     budgetService;
    private final FattyLiverProgressService progressService;
    private final WaterTrackingService      waterService;
    private final HealthMetricsService      metricsService;
    private final ExerciseService           exerciseService;
    private final AchievementService        achievementService;
    private final PetService               petService;

    private final Random random = new Random();

    private final String[] LATE_NIGHT_MESSAGES = {
        "这么晚还没睡，是不是又在想我？🌙",
        "月亮不睡你不睡，你是养生小宝贝~ 🐣",
        "深夜了，身体已经在排毒了呢~ 💫",
        "晚安~ 肝脏正在悄悄修复中... 🌙",
        "熬夜伤肝哦，但还是欢迎回来~ 💕",
        "深夜的你，比白天更认真呢！💪",
        "星空和你，都值得被温柔以待~ ✨",
        "今夜月色真美，你也... 💕",
        "身体在抗议了哦，今天早点休息吧~ 🌛",
        "深夜养生人，最有魅力了~ 😎"
    };

    public DashboardController(UricAcidBudgetService budgetService,
                                FattyLiverProgressService progressService,
                                WaterTrackingService waterService,
                                HealthMetricsService metricsService,
                                ExerciseService exerciseService,
                                AchievementService achievementService,
                                PetService petService) {
        this.budgetService   = budgetService;
        this.progressService = progressService;
        this.waterService    = waterService;
        this.metricsService  = metricsService;
        this.exerciseService = exerciseService;
        this.achievementService = achievementService;
        this.petService = petService;
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

        achievementService.checkAndAwardBadges();
        Map<String, Object> celebration = achievementService.checkMilestoneCelebration();
        if (celebration != null) {
            model.addAttribute("celebration", celebration);
        }
        Map<String, Object> surprise = achievementService.checkRandomSurprise();
        if (surprise != null) {
            model.addAttribute("surprise", surprise);
        }

        Pet pet = petService.updatePetStatus();
        model.addAttribute("pet", pet);
        model.addAttribute("petEncourage", petService.getEncourageMessage(pet));
        model.addAttribute("petStory", petService.getGrowthStory(pet));

        Map<String, Object> petInfo = petService.getPetInfo();
        model.addAttribute("isDead", petInfo.get("isDead"));
        model.addAttribute("willDieInDays", petInfo.get("willDieInDays"));

        LocalTime now = LocalTime.now();
        if (now.getHour() >= 23 || (now.getHour() == 23 && now.getMinute() >= 30)) {
            model.addAttribute("lateNightMode", true);
            model.addAttribute("lateNightMessage", LATE_NIGHT_MESSAGES[random.nextInt(LATE_NIGHT_MESSAGES.length)]);
        }

        return "dashboard";
    }
}
