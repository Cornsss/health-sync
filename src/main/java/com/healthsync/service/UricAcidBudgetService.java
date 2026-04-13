package com.healthsync.service;

import com.healthsync.dto.BudgetStatus;
import com.healthsync.dto.UricAcidBudgetDTO;
import com.healthsync.repository.HealthMetricsRepository;
import com.healthsync.repository.MealRecordRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UricAcidBudgetService {

    private static final double BASE_BUDGET_MG   = 150.0;
    private static final double FRUCTOSE_WARNING = 25.0;

    private final MealRecordRepository   mealRecordRepo;
    private final HealthMetricsRepository metricsRepo;

    public UricAcidBudgetService(MealRecordRepository mealRecordRepo,
                                  HealthMetricsRepository metricsRepo) {
        this.mealRecordRepo = mealRecordRepo;
        this.metricsRepo    = metricsRepo;
    }

    public UricAcidBudgetDTO calculateDailyBudget(LocalDate date) {
        double adjustedBudget = metricsRepo
                .findFirstByUricAcidIsNotNullOrderByRecordedAtDesc()
                .map(m -> {
                    int ua = m.getUricAcid();
                    if (ua > 480) return BASE_BUDGET_MG * 0.60;
                    if (ua > 420) return BASE_BUDGET_MG * 0.80;
                    return BASE_BUDGET_MG;
                })
                .orElse(BASE_BUDGET_MG);

        double consumed  = mealRecordRepo.sumPurineByDate(date).orElse(0.0);
        double fructose  = mealRecordRepo.sumFructoseByDate(date).orElse(0.0);
        double remaining = Math.max(adjustedBudget - consumed, 0);
        double rate      = adjustedBudget > 0 ? consumed / adjustedBudget * 100 : 0;

        return UricAcidBudgetDTO.builder()
                .date(date)
                .totalBudgetMg(adjustedBudget)
                .consumedMg(consumed)
                .remainingMg(remaining)
                .consumptionRate(rate)
                .progressPercent((int) Math.min(rate, 100))
                .status(resolveStatus(rate))
                .fructoseWarning(fructose > FRUCTOSE_WARNING)
                .fructoseTotalG(fructose)
                .build();
    }

    private BudgetStatus resolveStatus(double rate) {
        if (rate >= 100) return BudgetStatus.EXCEEDED;
        if (rate >= 80)  return BudgetStatus.WARNING;
        if (rate >= 50)  return BudgetStatus.CAUTION;
        return BudgetStatus.SAFE;
    }
}
