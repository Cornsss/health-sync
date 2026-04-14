package com.healthsync.service;

import com.healthsync.entity.WaterLog;
import com.healthsync.repository.WaterLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WaterTrackingService {

    public static final int DAILY_GOAL_ML = 2500;

    private final WaterLogRepository repository;
    private final PetService petService;

    public WaterTrackingService(WaterLogRepository repository, PetService petService) {
        this.repository = repository;
        this.petService = petService;
    }

    public WaterLog addWater(int amountMl) {
        WaterLog log = new WaterLog();
        log.setAmountMl(amountMl);
        log.setLoggedAt(LocalDateTime.now());
        WaterLog saved = repository.save(log);

        int todayTotal = getTodayTotal();
        if (todayTotal >= DAILY_GOAL_ML) {
            petService.recordWaterComplete();
        }
        return saved;
    }

    public int getTodayTotal() { return repository.sumByDate(LocalDate.now()).orElse(0); }

    public List<WaterLog> getTodayLogs() {
        return repository.findByLoggedDateOrderByLoggedAtDesc(LocalDate.now());
    }

    public int getDailyGoal() { return DAILY_GOAL_ML; }

    public int getProgressPercent(int totalMl) {
        return Math.min(100, totalMl * 100 / DAILY_GOAL_ML);
    }

    public List<Object[]> getWeeklyStats() {
        return repository.findDailySumBetween(LocalDate.now().minusDays(6), LocalDate.now());
    }

    public void deleteLog(Long id) {
        repository.deleteById(id);
    }
}
