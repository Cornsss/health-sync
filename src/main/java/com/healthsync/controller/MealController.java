package com.healthsync.controller;

import com.healthsync.dto.DailyMealDTO;
import com.healthsync.dto.GeneratedMealDTO;
import com.healthsync.dto.UricAcidBudgetDTO;
import com.healthsync.entity.MealRecord;
import com.healthsync.repository.MealRecordRepository;
import com.healthsync.service.MealGeneratorService;
import com.healthsync.service.UricAcidBudgetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/meals")
public class MealController {

    private final MealGeneratorService  generatorService;
    private final MealRecordRepository  mealRecordRepo;
    private final UricAcidBudgetService budgetService;

    public MealController(MealGeneratorService generatorService,
                           MealRecordRepository mealRecordRepo,
                           UricAcidBudgetService budgetService) {
        this.generatorService = generatorService;
        this.mealRecordRepo   = mealRecordRepo;
        this.budgetService    = budgetService;
    }

    @GetMapping
    public String index(Model model) {
        LocalDate today = LocalDate.now();
        List<MealRecord> todayMeals = mealRecordRepo.findByRecordedDateOrderByCreatedAtAsc(today);
        UricAcidBudgetDTO budget = budgetService.calculateDailyBudget(today);
        model.addAttribute("todayMeals", todayMeals);
        model.addAttribute("budget", budget);
        model.addAttribute("today", today);
        model.addAttribute("currentPage", "meals");
        return "meals/index";
    }

    @GetMapping("/library")
    public String library(Model model) {
        List<?> mealPlans = generatorService.getAllMealPlans();
        model.addAttribute("mealPlans", mealPlans);
        model.addAttribute("currentPage", "meals");
        return "meals/library";
    }

    @PostMapping("/generate")
    public String generate(RedirectAttributes attrs) {
        UricAcidBudgetDTO budget = budgetService.calculateDailyBudget(LocalDate.now());
        DailyMealDTO meal = generatorService.generateDailyMeal(budget.getTotalBudgetMg());
        GeneratedMealDTO dto = generatorService.buildGeneratedMealDTO(
                meal.getPlans(), meal.getTotalPurineMg(),
                meal.getTotalCalories(), meal.getTotalFructoseG());
        attrs.addFlashAttribute("generatedMeal", dto);
        attrs.addFlashAttribute("successMsg", "今日食谱已生成！嘌呤总量：" +
                String.format("%.1f", meal.getTotalPurineMg()) + "mg");
        return "redirect:/meals";
    }
}
