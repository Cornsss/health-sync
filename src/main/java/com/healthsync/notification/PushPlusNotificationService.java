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
public class PushPlusNotificationService {

    private static final Logger log = LoggerFactory.getLogger(PushPlusNotificationService.class);

    @Value("${pushplus.token:}")
    private String token;

    private final RestTemplate             restTemplate;
    private final FeishuPushLogRepository  pushLogRepo;

    private int todayWaterCache = 0;

    public PushPlusNotificationService(RestTemplate restTemplate,
                                        FeishuPushLogRepository pushLogRepo) {
        this.restTemplate = restTemplate;
        this.pushLogRepo  = pushLogRepo;
    }

    public void updateWaterCache(int totalMl) { this.todayWaterCache = totalMl; }

    @Scheduled(cron = "0 0,30 7-21 * * *")
    public void checkWaterReminder() {
        if (!ready()) return;
        LocalTime now = LocalTime.now();
        if (now.isBefore(LocalTime.of(7, 0)) || now.isAfter(LocalTime.of(21, 0))) return;

        Optional<FeishuPushLog> last = pushLogRepo.findFirstByPushTypeOrderByPushAtDesc("WATER_REMINDER_PUSHPLUS");
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
        send("WATER_REMINDER_PUSHPLUS", "Health-Sync 饮水提醒",
                String.format("今日已饮：%dml / 2500ml<br>进度：[%s]<br>还差：%dml<br><br>尿酸排出需要充足水分！",
                consumed, bar, remaining));
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void sendMorningReport() {
        if (!ready()) return;
        send("MORNING_REPORT_PUSHPLUS", "Health-Sync 早报 " + LocalDate.now(),
                "🎯 今日健康目标：<br>" +
                "• 饮水 ≥ 2500ml<br>" +
                "• 有效燃脂运动 ≥ 30 分钟<br>" +
                "• 避免动物内脏、浓汤、啤酒<br><br>" +
                "祝今天状态棒棒！💪");
    }

    @Scheduled(cron = "0 0 21 * * *")
    public void checkExerciseCheckin() {
        if (!ready()) return;
        send("EXERCISE_CHECKIN_PUSHPLUS", "运动打卡提醒",
                "🏃 今日是否完成运动计划？快去 Health-Sync 记录！");
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
            send("ALERT_PUSHPLUS", "⚕️ 健康指标预警", String.join("<br>", alerts));
    }

    public void sendBadgeNotification(Badge badge) {
        if (!ready()) return;
        send("BADGE_PUSHPLUS", "🎖️ 解锁新徽章：" + badge.getName(),
                badge.getIcon() + "<br><br><b>" + badge.getName() + "</b><br>" + badge.getDescription());
    }

    private void send(String pushType, String title, String content) {
        boolean ok = false; String err = null;
        try {
            Map<String, Object> payload = Map.of(
                    "token", token,
                    "title", title,
                    "content", content,
                    "template", "html");
            restTemplate.postForEntity("http://www.pushplus.plus/send", payload, String.class);
            ok = true;
            log.info("PushPlus [{}] OK", pushType);
        } catch (Exception e) {
            err = e.getMessage();
            log.warn("PushPlus [{}] failed: {}", pushType, err);
        }
        FeishuPushLog fpl = new FeishuPushLog();
        fpl.setPushType(pushType); fpl.setPayload(title + " | " + content);
        fpl.setSuccess(ok); fpl.setErrorMsg(err);
        pushLogRepo.save(fpl);
    }

    private boolean ready() { return token != null && !token.isBlank(); }
}