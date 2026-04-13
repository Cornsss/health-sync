package com.healthsync.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthsync.entity.HealthMetrics;
import com.healthsync.service.HealthMetricsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/metrics")
public class MetricsController {

    private final HealthMetricsService service;
    private final ObjectMapper         mapper;

    public MetricsController(HealthMetricsService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper  = mapper;
    }

    @GetMapping
    public String list(Model model) throws JsonProcessingException {
        List<HealthMetrics> recent = service.findRecent(60);
        model.addAttribute("allMetrics",  service.findRecentPaged());
        model.addAttribute("form",        new HealthMetrics());
        model.addAttribute("chartLabels", mapper.writeValueAsString(recent.stream().map(m -> m.getRecordedAt().toString()).toList()));
        model.addAttribute("uricData",    mapper.writeValueAsString(recent.stream().map(HealthMetrics::getUricAcid).toList()));
        model.addAttribute("weightData",  mapper.writeValueAsString(recent.stream().map(m -> m.getWeightKg() != null ? m.getWeightKg().doubleValue() : null).toList()));
        model.addAttribute("ggtData",     mapper.writeValueAsString(recent.stream().map(m -> m.getGgt() != null ? m.getGgt().doubleValue() : null).toList()));
        model.addAttribute("altData",     mapper.writeValueAsString(recent.stream().map(m -> m.getAlt() != null ? m.getAlt().doubleValue() : null).toList()));
        model.addAttribute("currentPage", "metrics");
        return "metrics/index";
    }

    @PostMapping
    public String save(@ModelAttribute HealthMetrics metrics, RedirectAttributes attrs) {
        service.save(metrics);
        attrs.addFlashAttribute("successMsg", "指标记录成功！");
        return "redirect:/metrics";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attrs) {
        service.delete(id);
        attrs.addFlashAttribute("successMsg", "记录已删除");
        return "redirect:/metrics";
    }
}
