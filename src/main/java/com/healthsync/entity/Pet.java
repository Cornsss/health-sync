package com.healthsync.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name = "猪皮";

    @Column(nullable = false, length = 20)
    private String type = "cat";

    @Column(nullable = false)
    private Integer level = 1;

    @Column(nullable = false)
    private Integer experience = 0;

    @Column(nullable = false)
    private Integer maxExperience = 100;

    @Column(nullable = false, length = 20)
    private String mood = "happy";

    @Column(nullable = false)
    private Integer hunger = 100;

    @Column(nullable = false)
    private Integer todayWaterComplete = 0;

    @Column(nullable = false)
    private Integer todayExerciseComplete = 0;

    @Column(nullable = false)
    private LocalDateTime lastFedTime = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime lastInteraction = LocalDateTime.now();

    @Column(nullable = false)
    private String status = "alive";

    @Column(nullable = false)
    private Integer daysAlive = 0;

    @Column
    private LocalDateTime deathTime;

    public Pet() {}

    public Pet(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) { this.experience = experience; }

    public Integer getMaxExperience() { return maxExperience; }
    public void setMaxExperience(Integer maxExperience) { this.maxExperience = maxExperience; }

    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    public Integer getHunger() { return hunger; }
    public void setHunger(Integer hunger) { this.hunger = hunger; }

    public Integer getTodayWaterComplete() { return todayWaterComplete; }
    public void setTodayWaterComplete(Integer todayWaterComplete) { this.todayWaterComplete = todayWaterComplete; }

    public Integer getTodayExerciseComplete() { return todayExerciseComplete; }
    public void setTodayExerciseComplete(Integer todayExerciseComplete) { this.todayExerciseComplete = todayExerciseComplete; }

    public LocalDateTime getLastFedTime() { return lastFedTime; }
    public void setLastFedTime(LocalDateTime lastFedTime) { this.lastFedTime = lastFedTime; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastInteraction() { return lastInteraction; }
    public void setLastInteraction(LocalDateTime lastInteraction) { this.lastInteraction = lastInteraction; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getDaysAlive() { return daysAlive; }
    public void setDaysAlive(Integer daysAlive) { this.daysAlive = daysAlive; }

    public LocalDateTime getDeathTime() { return deathTime; }
    public void setDeathTime(LocalDateTime deathTime) { this.deathTime = deathTime; }

    public String getMoodEmoji() {
        if ("dead".equals(status)) {
            return "💀";
        }
        return switch (mood) {
            case "happy" -> "😺";
            case "excited" -> "😻";
            case "sad" -> "😿";
            case "sleepy" -> "🙀";
            case "hungry" -> "😾";
            default -> "😺";
        };
    }

    public String getLevelTitle() {
        if ("dead".equals(status)) {
            return "已故";
        }
        if (level < 5) return "小奶猫";
        if (level < 10) return "小猪皮";
        if (level < 20) return "猪皮";
        if (level < 30) return "胖猪皮";
        if (level < 50) return "猪猪";
        return "猪神";
    }

    public String getAvatar() {
        if ("dead".equals(status)) {
            return "💀";
        }
        String base = switch (mood) {
            case "happy" -> "🐱";
            case "excited" -> "😻";
            case "sad" -> "😿";
            case "sleepy" -> "🙀";
            case "hungry" -> "😾";
            default -> "😺";
        };
        if (level >= 30) {
            return "🦁";
        } else if (level >= 20) {
            return "🐯";
        } else if (level >= 10) {
            return "🐱";
        }
        return base;
    }
}