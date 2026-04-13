package com.healthsync.init;

import com.healthsync.entity.FoodLibrary;
import com.healthsync.entity.MealPlan;
import com.healthsync.entity.MealPlanItem;
import com.healthsync.repository.FoodLibraryRepository;
import com.healthsync.repository.MealPlanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final FoodLibraryRepository foodRepo;
    private final MealPlanRepository    planRepo;

    public DataInitializer(FoodLibraryRepository foodRepo, MealPlanRepository planRepo) {
        this.foodRepo = foodRepo;
        this.planRepo = planRepo;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        if (foodRepo.count() > 0) { log.info("Data already exists, skipping init."); return; }
        log.info("Seeding food library and meal plans...");
        Map<String, FoodLibrary> foods = seedFoods();
        seedMealPlans(foods);
        log.info("Done: {} foods, {} plans", foodRepo.count(), planRepo.count());
    }

    private Map<String, FoodLibrary> seedFoods() {
        Map<String, FoodLibrary> m = new LinkedHashMap<>();
        // 碳水
        m.put("大米",     f("大米",     "碳水", 17,  0.3, 0,    130));
        m.put("燕麦",     f("燕麦",     "碳水", 25,  1.7, 0,    389));
        m.put("全麦面包", f("全麦面包", "碳水", 18,  2.0, 0,    247));
        m.put("土豆",     f("土豆",     "碳水",  3,  0.1, 0,     77));
        m.put("全麦面条", f("全麦面条", "碳水", 20,  1.5, 0,    350));
        // 蔬菜
        m.put("南瓜",   f("南瓜",   "蔬菜",  2, 0.1, 1.0,  26));
        m.put("白菜",   f("白菜",   "蔬菜", 12, 0.2, 1.0,  25));
        m.put("黄瓜",   f("黄瓜",   "蔬菜", 14, 0.1, 0.5,  16));
        m.put("胡萝卜", f("胡萝卜", "蔬菜",  8, 0.2, 1.0,  41));
        m.put("番茄",   f("番茄",   "蔬菜", 11, 0.2, 1.5,  18));
        m.put("西兰花", f("西兰花", "蔬菜", 50, 0.4, 0.5,  34));
        m.put("菠菜",   f("菠菜",   "蔬菜", 57, 0.4, 0.1,  23));
        m.put("芹菜",   f("芹菜",   "蔬菜", 19, 0.2, 0.1,  16));
        m.put("冬瓜",   f("冬瓜",   "蔬菜",  3, 0.2, 0.3,  13));
        m.put("木耳",   f("木耳",   "蔬菜",  8, 0.2, 0.0,  21));
        // 蛋白质
        m.put("鸡蛋",   f("鸡蛋",   "蛋白质",   3, 11.1, 0, 155));
        m.put("豆腐",   f("豆腐",   "蛋白质",  55,  4.8, 0,  76));
        m.put("牛奶",   f("牛奶",   "蛋白质",   1,  3.5, 0,  61));
        // 水果
        m.put("苹果",  f("苹果",  "水果", 0.9, 0.2, 10.0, 52));
        m.put("梨",    f("梨",    "水果", 1.1, 0.1,  6.0, 57));
        m.put("蓝莓",  f("蓝莓",  "水果", 2.0, 0.3,  3.5, 57));
        m.put("樱桃",  f("樱桃",  "水果", 7.0, 0.2,  6.0, 50));
        foodRepo.saveAll(m.values());
        // Refresh with persisted entities
        Map<String, FoodLibrary> saved = new LinkedHashMap<>();
        m.forEach((k, v) -> saved.put(k, foodRepo.findByName(k).orElse(v)));
        return saved;
    }

    private void seedMealPlans(Map<String, FoodLibrary> f) {
        // Breakfast
        plan("BREAKFAST","燕麦南瓜牛奶粥","低嘌呤早餐，β-葡聚糖降胆固醇",
                Map.of(f.get("燕麦"),50.0, f.get("南瓜"),100.0, f.get("牛奶"),150.0));
        plan("BREAKFAST","白粥水煮蛋配白菜","清淡易消化，保护肾脏",
                Map.of(f.get("大米"),60.0, f.get("鸡蛋"),60.0, f.get("白菜"),100.0));
        plan("BREAKFAST","全麦面包配牛奶苹果","粗粮+优质蛋白，稳血糖",
                Map.of(f.get("全麦面包"),80.0, f.get("牛奶"),200.0, f.get("苹果"),100.0));
        // Lunch
        plan("LUNCH","清炒时蔬豆腐米饭","低嘌呤，不升尿酸",
                Map.of(f.get("大米"),100.0, f.get("西兰花"),100.0,
                        f.get("胡萝卜"),80.0, f.get("豆腐"),80.0));
        plan("LUNCH","番茄鸡蛋全麦面","高番茄红素，低脂低嘌呤",
                Map.of(f.get("全麦面条"),100.0, f.get("番茄"),150.0, f.get("鸡蛋"),60.0));
        plan("LUNCH","冬瓜豆腐汤配米饭","冬瓜利水，有助尿酸排泄",
                Map.of(f.get("大米"),100.0, f.get("冬瓜"),150.0, f.get("豆腐"),100.0));
        // Dinner
        plan("DINNER","南瓜稀饭","清淡晚餐，利肝脏修复",
                Map.of(f.get("大米"),60.0, f.get("南瓜"),120.0, f.get("芹菜"),80.0));
        plan("DINNER","蒸蛋配焯水蔬菜","最低负担晚餐，肝功异常期适用",
                Map.of(f.get("鸡蛋"),80.0, f.get("白菜"),150.0, f.get("大米"),50.0));
        plan("DINNER","番茄豆腐菠菜汤","维C促进尿酸溶解",
                Map.of(f.get("豆腐"),80.0, f.get("番茄"),100.0, f.get("菠菜"),80.0));
        // Snack
        plan("SNACK","苹果","低嘌呤水果",Map.of(f.get("苹果"),150.0));
        plan("SNACK","蓝莓","抗氧化，低嘌呤低果糖",Map.of(f.get("蓝莓"),100.0));
        plan("SNACK","牛奶","优质蛋白，促尿酸排泄",Map.of(f.get("牛奶"),200.0));
    }

    private void plan(String mealType, String name, String desc, Map<FoodLibrary,Double> items) {
        MealPlan p = new MealPlan();
        p.setMealType(mealType); p.setPlanName(name); p.setDescription(desc);
        p = planRepo.save(p);
        for (var e : items.entrySet()) {
            MealPlanItem item = new MealPlanItem();
            item.setPlan(p); item.setFood(e.getKey());
            item.setAmountG(BigDecimal.valueOf(e.getValue()));
            p.getItems().add(item);
        }
        planRepo.save(p);
    }

    private FoodLibrary f(String name, String cat, double purine, double fat, double fructose, double cal) {
        FoodLibrary food = new FoodLibrary();
        food.setName(name); food.setCategory(cat);
        food.setPurinePerHundredG(BigDecimal.valueOf(purine));
        food.setFatPerHundredG(BigDecimal.valueOf(fat));
        food.setFructosePerHundredG(BigDecimal.valueOf(fructose));
        food.setCaloriesPerHundredG(BigDecimal.valueOf(cal));
        return food;
    }
}
