package com.healthsync.notification;

import com.healthsync.entity.Badge;
import com.healthsync.entity.FeishuPushLog;
import com.healthsync.entity.HealthMetrics;
import com.healthsync.repository.FeishuPushLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class FeishuNotificationService {

    private static final Logger log = LoggerFactory.getLogger(FeishuNotificationService.class);

    @Value("${feishu.webhook.url:}")
    private String webhookUrl;

    private final RestTemplate             restTemplate;
    private final FeishuPushLogRepository  pushLogRepo;

    // Avoid circular dependency: only use these via method params from controllers/services
    private int todayWaterCache = 0;

    public FeishuNotificationService(RestTemplate restTemplate,
                                      FeishuPushLogRepository pushLogRepo) {
        this.restTemplate = restTemplate;
        this.pushLogRepo  = pushLogRepo;
    }

    /** Called by WaterController to keep water total in sync for scheduled check */
    public void updateWaterCache(int totalMl) { this.todayWaterCache = totalMl; }

    // ── 每30分钟检查：距上次饮水提醒 >= 90 分钟且在 7:00-21:00 则推送 ──
    @Scheduled(cron = "0 0,30 7-21 * * *")
    public void checkWaterReminder() {
        if (!ready()) return;
        LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.of(7, 0)) || now.isAfter(LocalTime.of(21, 0))) return;

        Optional<FeishuPushLog> last = pushLogRepo.findFirstByPushTypeOrderByPushAtDesc("WATER_REMINDER");
        boolean shouldPush = last.map(l -> {
            long mins = java.time.Duration.between(l.getPushAt(), LocalDateTime.now()).toMinutes();
            return mins >= 90;
        }).orElse(true);
        if (!shouldPush) return;

        int consumed  = todayWaterCache;
        int remaining = Math.max(2500 - consumed, 0);
        if (remaining <= 0) return;

        int blocks   = Math.min(consumed / 250, 10);
        String bar   = "█".repeat(blocks) + "░".repeat(10 - blocks);
        send("WATER_REMINDER", String.format(
                "💧 **饮水提醒**\n今日已饮：%dml / 2500ml\n进度：[%s]\n还差：**%dml**\n\n_尿酸排出需要充足水分！_",
                consumed, bar, remaining));
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void sendMorningReport() {
        if (!ready()) return;
        send("MORNING_REPORT", String.format(
                "🌅 **Health-Sync 早报** %s\n\n🎯 今日健康目标：\n" +
                "  • 饮水 ≥ 2500ml\n  • 有效燃脂运动 ≥ 30 分钟\n" +
                "  • 避免动物内脏、浓汤、啤酒\n\n祝今天状态棒棒！💪", LocalDate.now()));
    }

    @Scheduled(cron = "0 0 21 * * *")
    public void checkExerciseCheckin() {
        if (!ready()) return;
        send("EXERCISE_CHECKIN", "🏃 **运动打卡提醒**\n今日是否完成运动计划？快去 Health-Sync 记录！");
    }

    public void sendMetricAlert(HealthMetrics m) {
        if (!ready()) return;
        List<String> alerts = new ArrayList<>();
        if (m.getUricAcid() != null && m.getUricAcid() > 480)
            alerts.add(String.format("🔴 尿酸 %dμmol/L（严重超标）", m.getUricAcid()));
        else if (m.getUricAcid() != null && m.getUricAcid() > 420)
            alerts.add(String.format("🟠 尿酸 %dμmol/L（轻度超标）", m.getUricAcid()));
        if (m.getGgt() != null && m.getGgt().doubleValue() > 60)
            alerts.add(String.format("🟠 GGT %.1f U/L（偏高）", m.getGgt()));
        if (!alerts.isEmpty())
            send("ALERT", "**⚕️ 健康指标预警**\n\n" + String.join("\n", alerts));
    }

    public void sendBadgeNotification(Badge badge) {
        if (!ready()) return;
        send("BADGE", badge.getIcon() + " **🎖️ 解锁新徽章！**\n\n" +
                "**" + badge.getName() + "**\n" +
                badge.getDescription());
    }

    private void send(String pushType, String content) {
        boolean ok = false; String err = null;
        try {
            Map<String, Object> payload = Map.of(
                    "msg_type", "interactive",
                    "card", Map.of(
                            "header", Map.of("title", Map.of("tag", "plain_text", "content", "Health-Sync"),
                                    "template", "blue"),
                            "elements", List.of(Map.of("tag", "markdown", "content", content))));
            restTemplate.postForEntity(webhookUrl, payload, String.class);
            ok = true;
            log.info("Feishu [{}] OK", pushType);
        } catch (Exception e) {
            err = e.getMessage();
            log.warn("Feishu [{}] failed: {}", pushType, err);
        }
        FeishuPushLog fpl = new FeishuPushLog();
        fpl.setPushType(pushType); fpl.setPayload(content);
        fpl.setSuccess(ok); fpl.setErrorMsg(err);
        pushLogRepo.save(fpl);
    }

    private boolean ready() { return webhookUrl != null && !webhookUrl.isBlank(); }
}
