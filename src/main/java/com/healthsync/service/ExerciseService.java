package com.healthsync.service;

import com.healthsync.entity.ExerciseLog;
import com.healthsync.repository.ExerciseLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExerciseService {

    private static final int MAX_HR = 185;
    public static final int FAT_BURN_LOW  = (int)(MAX_HR * 0.60);
    public static final int FAT_BURN_HIGH = (int)(MAX_HR * 0.70);

    private final ExerciseLogRepository repository;
    private final PetService petService;

    public ExerciseService(ExerciseLogRepository repository, PetService petService) {
        this.repository = repository;
        this.petService = petService;
    }

    public ExerciseLog save(ExerciseLog log) {
        if (log.getFatBurnMin() == null && log.getAvgHeartRate() != null) {
            int hr = log.getAvgHeartRate();
            log.setFatBurnMin((hr >= FAT_BURN_LOW && hr <= FAT_BURN_HIGH) ? log.getDurationMin() : 0);
        }
        if (log.getFatBurnMin() == null) log.setFatBurnMin(0);
        ExerciseLog saved = repository.save(log);

        if (log.getDurationMin() != null && log.getDurationMin() >= 30) {
            petService.recordExerciseComplete();
        }
        return saved;
    }

    public List<ExerciseLog> getTodayLogs() {
        return repository.findByExerciseDateOrderByCreatedAtDesc(LocalDate.now());
    }

    public List<ExerciseLog> getWeeklyLogs() {
        return repository.findByExerciseDateBetweenOrderByExerciseDateAsc(
                LocalDate.now().minusDays(6), LocalDate.now());
    }

    public int getWeeklyFatBurnMin() {
        Integer v = repository.sumFatBurnMinutesBetween(LocalDate.now().minusDays(6), LocalDate.now());
        return v != null ? v : 0;
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
