package com.healthsync.service;

import com.healthsync.entity.Pet;
import com.healthsync.repository.PetRepository;
import com.healthsync.repository.WaterLogRepository;
import com.healthsync.repository.ExerciseLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PetService {

    private static final Logger log = LoggerFactory.getLogger(PetService.class);

    private final PetRepository petRepo;
    private final WaterLogRepository waterLogRepo;
    private final ExerciseLogRepository exerciseLogRepo;

    public PetService(PetRepository petRepo, WaterLogRepository waterLogRepo,
                     ExerciseLogRepository exerciseLogRepo) {
        this.petRepo = petRepo;
        this.waterLogRepo = waterLogRepo;
        this.exerciseLogRepo = exerciseLogRepo;
    }

    @Transactional
    public Pet getOrCreatePet() {
        Optional<Pet> existing = petRepo.findFirstPet();
        if (existing.isPresent()) {
            return existing.get();
        }
        Pet pet = new Pet("猪皮");
        pet.setStatus("alive");
        pet.setDaysAlive(0);
        return petRepo.save(pet);
    }

    @Transactional
    public Pet updatePetStatus() {
        Pet pet = getOrCreatePet();
        LocalDate today = LocalDate.now();
        LocalDate lastDate = pet.getLastInteraction().toLocalDate();

        if ("dead".equals(pet.getStatus())) {
            return pet;
        }

        long daysInactive = ChronoUnit.DAYS.between(lastDate, today);

        if (daysInactive >= 3) {
            pet.setStatus("dead");
            pet.setDeathTime(LocalDateTime.now());
            pet.setMood("dead");
            log.info("Pet {} has died after {} days of inactivity", pet.getName(), daysInactive);
            return petRepo.save(pet);
        }

        if (daysInactive >= 1) {
            pet.setMood("sad");
            pet.setHunger(Math.max(0, pet.getHunger() - 30));
        }

        if (!lastDate.equals(today)) {
            pet.setTodayWaterComplete(0);
            pet.setTodayExerciseComplete(0);
            pet.setDaysAlive(pet.getDaysAlive() + 1);
        }

        pet.setLastInteraction(LocalDateTime.now());
        pet.setMood(determineMood(pet));

        return petRepo.save(pet);
    }

    @Transactional
    public Pet recordWaterComplete() {
        Pet pet = getOrCreatePet();

        if ("dead".equals(pet.getStatus())) {
            return pet;
        }

        if (pet.getTodayWaterComplete() == 0) {
            pet.setTodayWaterComplete(1);
            addExperience(pet, 10);
            pet.setMood("excited");
            pet.setLastInteraction(LocalDateTime.now());
            pet = petRepo.save(pet);
        }

        return pet;
    }

    @Transactional
    public Pet recordExerciseComplete() {
        Pet pet = getOrCreatePet();

        if ("dead".equals(pet.getStatus())) {
            return pet;
        }

        if (pet.getTodayExerciseComplete() == 0) {
            pet.setTodayExerciseComplete(1);
            addExperience(pet, 15);
            pet.setMood("happy");
            pet.setLastInteraction(LocalDateTime.now());
            pet = petRepo.save(pet);
        }

        return pet;
    }

    @Transactional
    public Pet revivePet() {
        Pet pet = getOrCreatePet();

        if (!"dead".equals(pet.getStatus())) {
            return pet;
        }

        pet.setStatus("alive");
        pet.setLevel(1);
        pet.setExperience(0);
        pet.setMaxExperience(100);
        pet.setMood("happy");
        pet.setHunger(100);
        pet.setTodayWaterComplete(0);
        pet.setTodayExerciseComplete(0);
        pet.setDaysAlive(0);
        pet.setDeathTime(null);
        pet.setLastInteraction(LocalDateTime.now());

        log.info("Pet {} has been revived!", pet.getName());
        return petRepo.save(pet);
    }

    private void addExperience(Pet pet, int amount) {
        pet.setExperience(pet.getExperience() + amount);

        while (pet.getExperience() >= pet.getMaxExperience()) {
            pet.setExperience(pet.getExperience() - pet.getMaxExperience());
            pet.setLevel(pet.getLevel() + 1);
            pet.setMaxExperience(pet.getMaxExperience() + 50);
            pet.setMood("excited");
            log.info("Pet leveled up! New level: {}", pet.getLevel());
        }
    }

    private String determineMood(Pet pet) {
        if ("dead".equals(pet.getStatus())) {
            return "dead";
        }

        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();

        if (hour >= 23 || hour < 6) {
            return "sleepy";
        }

        if (pet.getHunger() < 30) {
            return "hungry";
        }

        if (pet.getTodayWaterComplete() == 1 && pet.getTodayExerciseComplete() == 1) {
            return "excited";
        }

        if (pet.getHunger() < 60) {
            return "sad";
        }

        return "happy";
    }

    public String getGrowthStory(Pet pet) {
        if ("dead".equals(pet.getStatus())) {
            return "💀 " + pet.getName() + "已经离开了...但你可以复活它";
        }

        int level = pet.getLevel();
        String name = pet.getName();

        if (level < 3) {
            return "🐣 " + name + "刚刚来到你身边，还是一只软萌的小奶猫~";
        } else if (level < 5) {
            return "😺 " + name + "已经是一只活泼的小猪皮啦！";
        } else if (level < 10) {
            return "🐱 " + name + "正在健康成长，毛色越来越橘了！";
        } else if (level < 15) {
            return "😸 " + name + "开始发福了，脸都圆了！";
        } else if (level < 20) {
            return "🐯 " + name + "变成了小胖猪，撸起来超舒服~";
        } else if (level < 30) {
            return "🦁 " + name + "已经是一只威严的大猪猪了！";
        } else if (level < 50) {
            return "👑 " + name + "被大家称为'猪神'，超级尊贵！";
        } else {
            return "🌟 " + name + "已经超神！传说级别的猪猪！";
        }
    }

    public String getEncourageMessage(Pet pet) {
        String name = pet.getName();

        if ("dead".equals(pet.getStatus())) {
            return "💀 " + name + "需要你的帮助！点击复活按钮让它重获新生";
        }

        if (pet.getMood().equals("hungry")) {
            return name + "很虚弱了...快完成喝水和运动任务来救救它！";
        }

        if (pet.getMood().equals("sad")) {
            return name + "有点难受...别忘了今天的健康任务哦~";
        }

        if (pet.getTodayWaterComplete() == 0) {
            return "💧 " + name + "需要喝水！去记录喝水量吧~";
        }

        if (pet.getTodayExerciseComplete() == 0) {
            return "🏃 " + name + "想运动了！去完成运动任务吧~";
        }

        if (pet.getTodayWaterComplete() == 1 && pet.getTodayExerciseComplete() == 1) {
            return "🎉 " + name + "今天超开心！你太棒了！";
        }

        return "😺 " + name + "在等你回来~";
    }

    public Map<String, Object> getPetInfo() {
        Pet pet = getOrCreatePet();
        Map<String, Object> info = new HashMap<>();
        info.put("pet", pet);
        info.put("status", pet.getStatus());
        info.put("isDead", "dead".equals(pet.getStatus()));

        LocalDate today = LocalDate.now();
        LocalDate lastDate = pet.getLastInteraction().toLocalDate();
        long daysInactive = ChronoUnit.DAYS.between(lastDate, today);
        info.put("daysInactive", daysInactive);
        info.put("willDieInDays", Math.max(0, 3 - daysInactive));

        return info;
    }
}
