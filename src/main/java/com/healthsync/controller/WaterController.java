package com.healthsync.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthsync.entity.WaterLog;
import com.healthsync.notification.FeishuNotificationService;
import com.healthsync.service.WaterTrackingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/water")
public class WaterController {

    private final WaterTrackingService      service;
    private final FeishuNotificationService feishu;
    private final ObjectMapper              mapper;

    public WaterController(WaterTrackingService service,
                            FeishuNotificationService feishu,
                            ObjectMapper mapper) {
        this.service = service;
        this.feishu  = feishu;
        this.mapper  = mapper;
    }

    @GetMapping
    public String index(Model model) throws JsonProcessingException {
        int todayTotal = service.getTodayTotal();
        feishu.updateWaterCache(todayTotal);

        List<Object[]> rawStats = service.getWeeklyStats();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd");
        Map<String, Integer> dailyMap = new LinkedHashMap<>();
        for (int i = 6; i >= 0; i--) dailyMap.put(LocalDate.now().minusDays(i).format(fmt), 0);
        for (Object[] row : rawStats) {
            LocalDate d = (LocalDate) row[0];
            Number v    = (Number) row[1];
            dailyMap.put(d.format(fmt), v != null ? v.intValue() : 0);
        }

        model.addAttribute("todayTotal",  todayTotal);
        model.addAttribute("goalMl",      WaterTrackingService.DAILY_GOAL_ML);
        model.addAttribute("progressPct", service.getProgressPercent(todayTotal));
        model.addAttribute("todayLogs",   service.getTodayLogs());
        model.addAttribute("weekLabels",  mapper.writeValueAsString(new ArrayList<>(dailyMap.keySet())));
        model.addAttribute("weekData",    mapper.writeValueAsString(new ArrayList<>(dailyMap.values())));
        model.addAttribute("currentPage", "water");
        return "water/index";
    }

    @PostMapping("/add")
    public String addWater(@RequestParam int amountMl, RedirectAttributes attrs) {
        service.addWater(amountMl);
        feishu.updateWaterCache(service.getTodayTotal());
        attrs.addFlashAttribute("successMsg", "已记录 " + amountMl + " ml 饮水！");
        return "redirect:/water";
    }
}
