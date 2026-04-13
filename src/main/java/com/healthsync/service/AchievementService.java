package com.healthsync.service;

import com.healthsync.entity.*;
import com.healthsync.notification.FeishuNotificationService;
import com.healthsync.notification.PushPlusNotificationService;
import com.healthsync.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class AchievementService {

    private static final Logger log = LoggerFactory.getLogger(AchievementService.class);

    private final BadgeRepository badgeRepo;
    private final UserBadgeRepository userBadgeRepo;
    private final WaterLogRepository waterLogRepo;
    private final MealRecordRepository mealRecordRepo;
    private final ExerciseLogRepository exerciseLogRepo;
    private final HealthMetricsRepository metricsRepo;
    private final PushPlusNotificationService pushPlus;
    private final FeishuNotificationService feishu;

    private final Random random = new Random();

    public AchievementService(BadgeRepository badgeRepo, UserBadgeRepository userBadgeRepo,
                              WaterLogRepository waterLogRepo, MealRecordRepository mealRecordRepo,
                              ExerciseLogRepository exerciseLogRepo, HealthMetricsRepository metricsRepo,
                              PushPlusNotificationService pushPlus, FeishuNotificationService feishu) {
        this.badgeRepo = badgeRepo;
        this.userBadgeRepo = userBadgeRepo;
        this.waterLogRepo = waterLogRepo;
        this.mealRecordRepo = mealRecordRepo;
        this.exerciseLogRepo = exerciseLogRepo;
        this.metricsRepo = metricsRepo;
        this.pushPlus = pushPlus;
        this.feishu = feishu;
    }

    @Transactional
    public void initBadges() {
        if (badgeRepo.count() > 0) return;
        List<Badge> badges = List.of(
            new Badge("WATER_7", "水水怪", "连续7天饮水达标", "water", 7, "💧"),
            new Badge("WATER_30", "水利局长", "连续30天饮水达标", "water", 30, "🌊"),
            new Badge("PURINE_7", "抗嘌战士", "连续7天嘌呤不超标", "purine", 7, "⚔️"),
            new Badge("PURINE_30", "尿酸克星", "连续30天嘌呤不超标", "purine", 30, "🛡️"),
            new Badge("EXERCISE_7", "运动达人", "连续7天完成运动", "exercise", 7, "🏃"),
            new Badge("EXERCISE_30", "健身狂人", "连续30天完成运动", "exercise", 30, "💪"),
            new Badge("EARLY_BIRD_7", "比我还自律的一周", "连续7天早起打卡", "milestone", 7, "🌅"),
            new Badge("EARLY_BIRD_14", "早起王者", "连续14天早起打卡", "milestone", 14, "☀️"),
            new Badge("PERFECT_DAY", "完美一天", "今日所有健康任务全部完成", "milestone", null, "✨"),
            new Badge("NEED_HUG", "需要抱抱", "连续3天没记录啦", "hidden", 3, "🤗"),
            new Badge("FIRST_RECORD", "健康起步", "完成首次健康记录", "milestone", null, "🌱"),
            new Badge("MEAL_PLAN_MASTER", "食谱大师", "使用食谱生成功能", "hidden", null, "📖"),
            new Badge("NIGHT_OWL", "夜猫子", "凌晨1点还在记录", "hidden", null, "🦉"),
            new Badge("WEEKEND_WARRIOR", "自律达人", "周末也不放松", "hidden", null, "🌟")
        );
        badgeRepo.saveAll(badges);
        log.info("Initialized {} badges", badges.size());
    }

    @Transactional
    public List<Map<String, Object>> checkAndAwardBadges() {
        List<Map<String, Object>> newBadges = new ArrayList<>();
        initBadges();

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        checkWaterBadges(today, newBadges);
        checkPurineBadges(today, newBadges);
        checkExerciseBadges(today, newBadges);
        checkEarlyBirdBadges(today, newBadges);
        checkNeedHugBadge(today, newBadges);
        checkPerfectDayBadge(today, newBadges);
        checkHiddenBadges(today, newBadges);

        return newBadges;
    }

    private void checkWaterBadges(LocalDate today, List<Map<String, Object>> newBadges) {
        int consecutiveDays = countConsecutiveWaterDays(today);
        checkBadge("WATER_7", consecutiveDays, 7, newBadges);
        checkBadge("WATER_30", consecutiveDays, 30, newBadges);
    }

    private void checkPurineBadges(LocalDate today, List<Map<String, Object>> newBadges) {
        int consecutiveDays = countConsecutivePurineDays(today);
        checkBadge("PURINE_7", consecutiveDays, 7, newBadges);
        checkBadge("PURINE_30", consecutiveDays, 30, newBadges);
    }

    private void checkExerciseBadges(LocalDate today, List<Map<String, Object>> newBadges) {
        int consecutiveDays = countConsecutiveExerciseDays(today);
        checkBadge("EXERCISE_7", consecutiveDays, 7, newBadges);
        checkBadge("EXERCISE_30", consecutiveDays, 30, newBadges);
    }

    private void checkHealthBadges(List<Map<String, Object>> newBadges) {
        if (!userBadgeRepo.existsByBadge_Code("URIC_NORMAL")) {
            Optional<HealthMetrics> latestMetric = metricsRepo.findFirstByOrderByRecordedAtDesc();
            if (latestMetric.isPresent() && latestMetric.get().getUricAcid() != null
                    && latestMetric.get().getUricAcid() < 420) {
                awardBadge("URIC_NORMAL", newBadges);
            }
        }
    }

    private void checkHiddenBadges(LocalDate today, List<Map<String, Object>> newBadges) {
        LocalTime now = LocalTime.now();
        if (now.getHour() == 1 && !userBadgeRepo.existsByBadge_Code("NIGHT_OWL")) {
            awardBadge("NIGHT_OWL", newBadges);
        }

        LocalDate yesterday = today.minusDays(1);
        boolean yesterdayWeekend = yesterday.getDayOfWeek().getValue() >= 6;
        boolean todayWeekend = today.getDayOfWeek().getValue() >= 6;
        if ((yesterdayWeekend || todayWeekend) && !userBadgeRepo.existsByBadge_Code("WEEKEND_WARRIOR")) {
            List<WaterLog> yesterdayWater = waterLogRepo.findByLoggedDateOrderByLoggedAtDesc(yesterday);
            List<WaterLog> todayWater = waterLogRepo.findByLoggedDateOrderByLoggedAtDesc(today);
            if (!yesterdayWater.isEmpty() || !todayWater.isEmpty()) {
                awardBadge("WEEKEND_WARRIOR", newBadges);
            }
        }
    }

    private void checkEarlyBirdBadges(LocalDate today, List<Map<String, Object>> newBadges) {
        int consecutiveEarlyDays = countConsecutiveEarlyBirdDays(today);
        checkBadge("EARLY_BIRD_7", consecutiveEarlyDays, 7, newBadges);
        checkBadge("EARLY_BIRD_14", consecutiveEarlyDays, 14, newBadges);
    }

    private int countConsecutiveEarlyBirdDays(LocalDate startDate) {
        int count = 0;
        LocalDate date = startDate;
        while (count < 100) {
            Optional<WaterLog> firstLog = waterLogRepo.findFirstByDateOrderByTimeAsc(date);
            if (firstLog.isPresent()) {
                int hour = firstLog.get().getLoggedAt().getHour();
                if (hour < 7) {
                    count++;
                    date = date.minusDays(1);
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return count;
    }

    private void checkNeedHugBadge(LocalDate today, List<Map<String, Object>> newBadges) {
        if (userBadgeRepo.existsByBadge_Code("NEED_HUG")) return;

        LocalDate threeDaysAgo = today.minusDays(3);
        LocalDate yesterday = today.minusDays(1);

        boolean hasLogs = false;
        for (LocalDate d = yesterday; !d.isBefore(threeDaysAgo); d = d.minusDays(1)) {
            List<WaterLog> logs = waterLogRepo.findByLoggedDateOrderByLoggedAtDesc(d);
            List<ExerciseLog> exercises = exerciseLogRepo.findByExerciseDateOrderByCreatedAtDesc(d);
            if (!logs.isEmpty() || !exercises.isEmpty()) {
                hasLogs = true;
                break;
            }
        }

        if (!hasLogs && waterLogRepo.findByLoggedDateOrderByLoggedAtDesc(yesterday).isEmpty()
                && exerciseLogRepo.findByExerciseDateOrderByCreatedAtDesc(yesterday).isEmpty()) {
            long daysSinceLastLog = java.time.temporal.ChronoUnit.DAYS.between(
                findLastLogDate(), today);
            if (daysSinceLastLog >= 3) {
                awardBadge("NEED_HUG", newBadges);
            }
        }
    }

    private LocalDate findLastLogDate() {
        Optional<WaterLog> lastWater = waterLogRepo.findFirstByOrderByLoggedAtDesc();
        if (lastWater.isPresent()) {
            return lastWater.get().getLoggedDate();
        }
        return LocalDate.now();
    }

    private void checkPerfectDayBadge(LocalDate today, List<Map<String, Object>> newBadges) {
        if (userBadgeRepo.existsByBadge_Code("PERFECT_DAY")) return;

        Optional<Integer> waterSum = waterLogRepo.sumByDate(today);
        List<ExerciseLog> exercises = exerciseLogRepo.findByExerciseDateOrderByCreatedAtDesc(today);

        boolean waterComplete = waterSum.isPresent() && waterSum.get() >= 2000;
        boolean exerciseComplete = exercises.stream()
                .anyMatch(e -> e.getDurationMin() != null && e.getDurationMin() >= 30);

        if (waterComplete && exerciseComplete) {
            awardBadge("PERFECT_DAY", newBadges);
        }
    }

    private int countConsecutiveWaterDays(LocalDate startDate) {
        int count = 0;
        LocalDate date = startDate;
        while (count < 100) {
            Optional<Integer> sum = waterLogRepo.sumByDate(date);
            if (sum.isPresent() && sum.get() >= 2000) {
                count++;
                date = date.minusDays(1);
            } else {
                break;
            }
        }
        return count;
    }

    private int countConsecutivePurineDays(LocalDate startDate) {
        int count = 0;
        LocalDate date = startDate;
        while (count < 100) {
            Optional<Double> totalPurine = mealRecordRepo.sumPurineByDate(date);
            if (totalPurine.isPresent() && totalPurine.get() > 0 && totalPurine.get() <= 400) {
                count++;
                date = date.minusDays(1);
            } else {
                break;
            }
        }
        return count;
    }

    private int countConsecutiveExerciseDays(LocalDate startDate) {
        int count = 0;
        LocalDate date = startDate;
        while (count < 100) {
            List<ExerciseLog> exercises = exerciseLogRepo.findByExerciseDateOrderByCreatedAtDesc(date);
            boolean hasValidExercise = exercises.stream()
                    .anyMatch(e -> e.getDurationMin() != null && e.getDurationMin() >= 30);
            if (hasValidExercise) {
                count++;
                date = date.minusDays(1);
            } else {
                break;
            }
        }
        return count;
    }

    private void checkBadge(String code, int currentStreak, int threshold, List<Map<String, Object>> newBadges) {
        if (currentStreak >= threshold && !userBadgeRepo.existsByBadge_Code(code)) {
            awardBadge(code, newBadges);
        }
    }

    @Transactional
    public void awardBadge(String code, List<Map<String, Object>> newBadges) {
        Optional<Badge> badgeOpt = badgeRepo.findByCode(code);
        if (badgeOpt.isEmpty()) return;
        if (userBadgeRepo.existsByBadge_Code(code)) return;

        Badge badge = badgeOpt.get();
        UserBadge userBadge = new UserBadge(badge);
        userBadgeRepo.save(userBadge);

        Map<String, Object> award = new HashMap<>();
        award.put("code", badge.getCode());
        award.put("name", badge.getName());
        award.put("icon", badge.getIcon());
        award.put("description", badge.getDescription());
        award.put("hidden", badge.getHidden());
        newBadges.add(award);

        log.info("Badge awarded: {}", badge.getName());
        notifyBadgeAward(badge);
    }

    private void notifyBadgeAward(Badge badge) {
        try {
            pushPlus.sendBadgeNotification(badge);
        } catch (Exception e) {
            log.warn("PushPlus notification failed: {}", e.getMessage());
        }
        try {
            feishu.sendBadgeNotification(badge);
        } catch (Exception e) {
            log.warn("Feishu notification failed: {}", e.getMessage());
        }
    }

    public List<Map<String, Object>> getAllBadges() {
        List<Badge> allBadges = badgeRepo.findAll();
        List<UserBadge> userBadges = userBadgeRepo.findAll();
        Set<Long> unlockedIds = new HashSet<>();
        userBadges.forEach(ub -> unlockedIds.add(ub.getBadge().getId()));

        List<Map<String, Object>> result = new ArrayList<>();
        for (Badge badge : allBadges) {
            Map<String, Object> map = new HashMap<>();
            map.put("code", badge.getCode());
            map.put("name", badge.getName());
            map.put("icon", badge.getIcon());
            map.put("description", badge.getDescription());
            map.put("category", badge.getCategory());
            map.put("unlocked", unlockedIds.contains(badge.getId()));
            result.add(map);
        }
        return result;
    }

    public Map<String, Object> checkRandomSurprise() {
        if (random.nextInt(100) < 20) {
            String[] surprises = {
                "💧 今日目标：喝够8杯水，尿酸悄悄降~",
                "🌱 小秘密：每减1kg体重，尿酸平均降0.7μmol/L哦！",
                "🏃 运动处方：每天快走40分钟，比跑步更护关节~",
                "🍎 水果黑名单：荔枝、榴莲、芒果，果糖炸弹要躲开！",
                "🛌 睡眠公式：深度睡眠1小时 = 肝脏修复3倍效率",
                "🥬 护肝王牌：每天一把西兰花，肝脏给你点赞！",
                "💧 喝水暗号：尿清如水，说明你很棒！",
                "🍵 饮品红榜：淡茶、柠檬水，浓汤Say No！",
                "🌙 夜间保养：23点前睡，肝脏需要深度睡眠来排毒~",
                "📏 腰围警报：男性>90cm/女性>85cm，脂肪肝风险↑↑",
                "🦐 海鲜攻略：告别高嘌呤鱼类，换成淡水鱼更健康！",
                "🍚 主食替换：用糙米替代1/3白米饭，纤维翻倍！",
                "🧂 减盐技巧：少放酱油，多放葱姜蒜提鲜~",
                "🚶 饭后百步：餐后散步30分钟，血糖稳稳的！",
                "📱 久坐警告：每坐1小时，起身活动5分钟吧~",
                "🍵 茶饮推荐：荷叶茶、玉米须茶，利尿排酸一把好手！",
                "😌 情绪管理：压力大时尿酸会升高，试试深呼吸~",
                "🥚 蛋白质来源：鸡蛋是优质蛋白，每天1-2个刚刚好！",
                "🥒 蔬菜之王：黄瓜利尿又低嘌呤，夏天必备~",
                "⏰ 服药提醒：按时吃药比什么都重要！",
                "🌡️ 体温监测：尿酸高的人，体温往往偏低哦~",
                "🫖 饮水时机：早起一杯温水，唤醒代谢加速器！",
                "🚴 有氧优先：游泳、骑车、走路，膝盖表示很开心~",
                "🥗 彩虹饮食：每天吃够5种颜色，维生素满满！",
                "📊 数据力量：记录就是在和健康对话~"
            };
            Map<String, Object> surprise = new HashMap<>();
            surprise.put("type", "surprise");
            surprise.put("message", surprises[random.nextInt(surprises.length)]);
            return surprise;
        }
        return null;
    }

    public Map<String, Object> checkMilestoneCelebration() {
        List<UserBadge> recentBadges = userBadgeRepo.findByNotifiedFalse();
        if (recentBadges.isEmpty()) return null;

        UserBadge latest = recentBadges.get(0);
        latest.setNotified(true);
        userBadgeRepo.save(latest);

        String code = latest.getBadge().getCode();
        String[] messages;

        if (code.startsWith("WATER")) {
            messages = new String[]{
                "💧 水利局长驾到！你的身体今天喝饱了吗？",
                "🌊 滴水穿石！坚持的力量太可怕了！",
                "💧 喝水王者！你的肾脏在唱歌~"
            };
        } else if (code.startsWith("PURINE")) {
            messages = new String[]{
                "⚔️ 抗嘌战士！尿酸被你收拾得服服帖帖！",
                "🛡️ 防御值拉满！痛风看了都想绕道走~",
                "⚔️ 嘌呤克星！你的饮食自律到可怕！"
            };
        } else if (code.startsWith("EXERCISE")) {
            messages = new String[]{
                "🏃 运动机器！汗水不会辜负你！",
                "💪 肌肉在尖叫！继续保持！",
                "🏃 每一步都算数！你在超越昨天的自己！"
            };
        } else if (code.startsWith("EARLY_BIRD")) {
            messages = new String[]{
                "🌅 早起鸟！自律的人最可怕（褒义）！",
                "☀️ 太阳都还没起床，你就已经在路上了！",
                "🌅 比你聪明的人还在睡，比你自律的人已经醒了~"
            };
        } else if (code.equals("PERFECT_DAY")) {
            messages = new String[]{
                "✨ 完美一天！今天你就是健康MVP！",
                "🌟 六边形战士！喝水+运动+饮食全部拿下！",
                "✨ 今天的表现，可以发朋友圈炫耀了！"
            };
        } else if (code.equals("NEED_HUG")) {
            messages = new String[]{
                "🤗 抱抱你！休息一下，明天继续加油！",
                "🤗 健康是马拉松，不是百米冲刺~",
                "🤗 偶尔休息也是为了走更远的路！"
            };
        } else {
            messages = new String[]{
                "🎉 恭喜解锁「" + latest.getBadge().getName() + "」成就！",
                "🏆 新成就Get！你比想象中更厉害！",
                "🌟 成就解锁！你在健康的路上又前进了一步！"
            };
        }

        Map<String, Object> celebration = new HashMap<>();
        celebration.put("type", "celebration");
        celebration.put("icon", latest.getBadge().getIcon());
        celebration.put("name", latest.getBadge().getName());
        celebration.put("message", messages[random.nextInt(messages.length)]);
        return celebration;
    }
}