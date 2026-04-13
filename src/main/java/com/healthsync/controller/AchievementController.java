package com.healthsync.controller;

import com.healthsync.service.AchievementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/achievements")
public class AchievementController {

    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping
    public String getAchievements(Model model) {
        List<Map<String, Object>> badges = achievementService.getAllBadges();
        model.addAttribute("badges", badges);
        return "achievements/index";
    }
}