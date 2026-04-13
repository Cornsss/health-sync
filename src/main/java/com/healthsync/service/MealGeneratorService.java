package com.healthsync.service;

import com.healthsync.dto.DailyMealDTO;
import com.healthsync.dto.GeneratedMealDTO;
import com.healthsync.entity.MealPlan;
import com.healthsync.entity.MealPlanItem;
import com.healthsync.entity.MealRecord;
import com.healthsync.repository.MealPlanRepository;
import com.healthsync.repository.MealRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MealGeneratorService {

    private static final Map<String, Double> BUDGET_RATIO = Map.of(
            "BREAKFAST", 0.20, "LUNCH", 0.40, "DINNER", 0.30, "SNACK", 0.10);

    private final MealPlanRepository   planRepo;
    private final MealRecordRepository recordRepo;

    public MealGeneratorService(MealPlanRepository planRepo, MealRecordRepository recordRepo) {
        this.planRepo   = planRepo;
        this.recordRepo = recordRepo;
    }

    @Transactional
    public DailyMealDTO generateDailyMeal(double totalBudget) {
        recordRepo.deleteByRecordedDate(LocalDate.now());

        Map<String, MealPlan> selected = new LinkedHashMap<>();
        double usedPurine = 0, usedCal = 0, usedFructose = 0;

        for (String mealType : List.of("BREAKFAST", "LUNCH", "DINNER", "SNACK")) {
            double mealBudget = totalBudget * BUDGET_RATIO.getOrDefault(mealType, 0.10);
            List<MealPlan> candidates = planRepo.findByMealTypeAndIsActiveTrue(mealType);
            List<MealPlan> safe = candidates.stream()
                    .filter(p -> calcPurine(p) <= mealBudget).collect(Collectors.toList());
            if (safe.isEmpty()) safe = candidates;
            if (safe.isEmpty()) continue;

            Collections.shuffle(safe);
            MealPlan chosen = safe.get(0);
            selected.put(mealType, chosen);

            double p = calcPurine(chosen), c = calcCal(chosen), f = calcFructose(chosen);
            usedPurine += p; usedCal += c; usedFructose += f;

            MealRecord rec = new MealRecord();
            rec.setRecordedDate(LocalDate.now());
            rec.setMealType(mealType);
            rec.setPlan(chosen);
            rec.setTotalPurineMg(BigDecimal.valueOf(p));
            rec.setTotalFructoseG(BigDecimal.valueOf(f));
            rec.setTotalCalories(BigDecimal.valueOf(c));
            rec.setIsRandomGenerated(true);
            recordRepo.save(rec);
        }

        return DailyMealDTO.builder()
                .date(LocalDate.now())
                .plans(selected)
                .totalPurineMg(round1(usedPurine))
                .totalCalories(round1(usedCal))
                .totalFructoseG(round1(usedFructose))
                .shoppingList(buildShoppingList(selected))
                .build();
    }

    private double calcPurine(MealPlan p)   { return p.getItems().stream().mapToDouble(MealPlanItem::getPurineMg).sum(); }
    private double calcCal(MealPlan p)      { return p.getItems().stream().mapToDouble(MealPlanItem::getCalories).sum(); }
    private double calcFructose(MealPlan p) { return p.getItems().stream().mapToDouble(MealPlanItem::getFructoseG).sum(); }
    private double round1(double v)         { return Math.round(v * 10.0) / 10.0; }

    private Map<String, Double> buildShoppingList(Map<String, MealPlan> plans) {
        Map<String, Double> list = new LinkedHashMap<>();
        plans.values().forEach(p -> p.getItems().forEach(item ->
                list.merge(item.getFood().getName(), item.getAmountG().doubleValue(), Double::sum)));
        return list;
    }

    public List<Map<String, Object>> getAllMealPlans() {
        List<MealPlan> plans = planRepo.findAll();
        return plans.stream().map(p -> {
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("id", p.getId());
            dto.put("planName", p.getPlanName());
            dto.put("description", p.getDescription());
            dto.put("mealType", p.getMealType());
            dto.put("totalPurineMg", calcPurine(p));
            dto.put("totalCalories", calcCal(p));
            dto.put("totalFructoseG", calcFructose(p));
            dto.put("foodNames", p.getItems().stream().map(i -> i.getFood().getName()).collect(Collectors.toList()));
            dto.put("items", p.getItems().stream().map(i -> {
                Map<String, Object> itemDto = new LinkedHashMap<>();
                itemDto.put("foodName", i.getFood().getName());
                itemDto.put("amountG", i.getAmountG());
                itemDto.put("purineMg", i.getPurineMg());
                itemDto.put("calories", i.getCalories());
                itemDto.put("cookingMethod", i.getCookingMethod());
                return itemDto;
            }).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

    public GeneratedMealDTO buildGeneratedMealDTO(Map<String, MealPlan> selected, double totalPurine, double totalCal, double totalFructose) {
        GeneratedMealDTO dto = new GeneratedMealDTO();
        dto.setTotalPurineMg(round1(totalPurine));
        dto.setTotalCalories(round1(totalCal));
        dto.setTotalFructoseG(round1(totalFructose));

        for (Map.Entry<String, MealPlan> entry : selected.entrySet()) {
            MealPlan plan = entry.getValue();
            GeneratedMealDTO.PlanInfo planInfo = new GeneratedMealDTO.PlanInfo();
            planInfo.setPlanName(plan.getPlanName());
            planInfo.setDescription(plan.getDescription());

            for (MealPlanItem item : plan.getItems()) {
                GeneratedMealDTO.ItemInfo itemInfo = new GeneratedMealDTO.ItemInfo();
                itemInfo.setFoodName(item.getFood().getName());
                itemInfo.setAmountG(item.getAmountG().doubleValue());
                itemInfo.setCookingMethod(item.getCookingMethod());
                planInfo.getItems().add(itemInfo);
            }

            dto.getPlans().put(entry.getKey(), planInfo);
        }

        dto.setShoppingList(buildShoppingList(selected));

        return dto;
    }
}
