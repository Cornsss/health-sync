package com.healthsync.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthsync.entity.ExerciseLog;
import com.healthsync.service.ExerciseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/exercise")
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final ObjectMapper    mapper;

    public ExerciseController(ExerciseService exerciseService, ObjectMapper mapper) {
        this.exerciseService = exerciseService;
        this.mapper          = mapper;
    }

    @GetMapping
    public String index(Model model) throws JsonProcessingException {
        List<ExerciseLog> weekly = exerciseService.getWeeklyLogs();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd");
        List<String>  labels    = new ArrayList<>();
        List<Integer> durations = new ArrayList<>();
        List<Integer> fatBurns  = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {
            LocalDate day = LocalDate.now().minusDays(i);
            labels.add(day.format(fmt));
            int dur = weekly.stream().filter(e -> e.getExerciseDate().equals(day))
                    .mapToInt(ExerciseLog::getDurationMin).sum();
            int fb = weekly.stream().filter(e -> e.getExerciseDate().equals(day))
                    .mapToInt(e -> e.getFatBurnMin() != null ? e.getFatBurnMin() : 0).sum();
            durations.add(dur);
            fatBurns.add(fb);
        }

        model.addAttribute("form",             new ExerciseLog());
        model.addAttribute("todayLogs",        exerciseService.getTodayLogs());
        model.addAttribute("weeklyLogs",       weekly);
        model.addAttribute("weeklyFatBurnMin", exerciseService.getWeeklyFatBurnMin());
        model.addAttribute("fatBurnLow",       ExerciseService.FAT_BURN_LOW);
        model.addAttribute("fatBurnHigh",      ExerciseService.FAT_BURN_HIGH);
        model.addAttribute("weekLabels",       mapper.writeValueAsString(labels));
        model.addAttribute("weekDurations",    mapper.writeValueAsString(durations));
        model.addAttribute("weekFatBurns",     mapper.writeValueAsString(fatBurns));
        model.addAttribute("currentPage", "exercise");
        return "exercise/index";
    }

    @PostMapping
    public String save(@ModelAttribute ExerciseLog log, RedirectAttributes attrs) {
        exerciseService.save(log);
        attrs.addFlashAttribute("successMsg", "运动记录已保存！");
        return "redirect:/exercise";
    }

    @PostMapping("/delete/{id}")
    public String deleteLog(@PathVariable Long id, RedirectAttributes attrs) {
        exerciseService.delete(id);
        attrs.addFlashAttribute("successMsg", "已删除运动记录");
        return "redirect:/exercise";
    }
}
