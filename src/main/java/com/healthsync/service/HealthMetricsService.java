package com.healthsync.service;

import com.healthsync.entity.HealthMetrics;
import com.healthsync.notification.FeishuNotificationService;
import com.healthsync.repository.HealthMetricsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class HealthMetricsService {

    private final HealthMetricsRepository  repository;
    private final FeishuNotificationService feishu;

    public HealthMetricsService(HealthMetricsRepository repository,
                                 FeishuNotificationService feishu) {
        this.repository = repository;
        this.feishu     = feishu;
    }

    public HealthMetrics save(HealthMetrics metrics) {
        HealthMetrics saved = repository.save(metrics);
        feishu.sendMetricAlert(saved);
        return saved;
    }

    public Optional<HealthMetrics> findLatest() { return repository.findFirstByOrderByRecordedAtDesc(); }

    public List<HealthMetrics> findRecent(int days) {
        return repository.findByRecordedAtBetweenOrderByRecordedAtAsc(
                LocalDate.now().minusDays(days), LocalDate.now());
    }

    public List<HealthMetrics> findRecentPaged() {
        return repository.findRecentPaged(PageRequest.of(0, 30));
    }

    public void delete(Long id) { repository.deleteById(id); }
}
