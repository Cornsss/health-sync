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
        // 杂粮
        m.put("小米",     f("小米",     "碳水",  7,  1.0, 0,    358));
        m.put("玉米",     f("玉米",     "碳水",  9,  1.2, 0,     86));
        m.put("薏米",     f("薏米",     "碳水", 25,  3.3, 0,    357));
        m.put("糙米",     f("糙米",     "碳水", 22,  1.8, 0,    370));
        m.put("山药",     f("山药",     "碳水",  6,  0.2, 0,     56));
        // 蔬菜补充
        m.put("西葫芦",   f("西葫芦",   "蔬菜",  7, 0.2, 0.8,  19));
        m.put("茄子",     f("茄子",     "蔬菜",  8, 0.2, 0.5,  25));
        m.put("洋葱",     f("洋葱",     "蔬菜", 13, 0.1, 2.0,  40));
        m.put("丝瓜",     f("丝瓜",     "蔬菜",  9, 0.2, 0.5,  20));
        m.put("豆芽",     f("豆芽",     "蔬菜", 44, 0.2, 0.0,  30));
        m.put("莲藕",     f("莲藕",     "蔬菜",  8, 0.2, 0.3,  74));
        m.put("红薯",     f("红薯",     "碳水",  5, 0.1, 1.5,  87));
        // 蛋白质
        m.put("鸡蛋",       f("鸡蛋",       "蛋白质",   3, 11.1, 0, 155));
        m.put("豆腐",       f("豆腐",       "蛋白质",  55,  4.8, 0,  76));
        m.put("牛奶",       f("牛奶",       "蛋白质",   1,  3.5, 0,  61));
        m.put("无糖酸奶",   f("无糖酸奶",   "蛋白质",   1,  3.0, 0,  60));
        m.put("北豆腐",     f("北豆腐",     "蛋白质",  68,  4.8, 0,  98));
        m.put("鸡胸肉",     f("鸡胸肉",     "蛋白质", 137,  5.0, 0, 133));
        // 水果
        m.put("苹果",  f("苹果",  "水果", 0.9, 0.2, 10.0, 52));
        m.put("梨",    f("梨",    "水果", 1.1, 0.1,  6.0, 57));
        m.put("蓝莓",  f("蓝莓",  "水果", 2.0, 0.3,  3.5, 57));
        m.put("樱桃",  f("樱桃",  "水果", 7.0, 0.2,  6.0, 50));
        m.put("草莓",  f("草莓",  "水果", 4.0, 0.3,  3.0, 33));
        // 调料/汤底
        m.put("海带",  f("海带",  "蔬菜",  8, 0.1, 0.0,  12));
        m.put("紫菜",  f("紫菜",  "蔬菜",  12, 0.3, 0.0,  35));
        m.put("银耳",  f("银耳",  "蔬菜",  6, 0.2, 0.0,  28));
        m.put("香菇",  f("香菇",  "蔬菜",  15, 0.3, 0.0,  26));
        m.put("金针菇", f("金针菇", "蔬菜", 18, 0.4, 0.0,  30));
        m.put("鸡腿肉", f("鸡腿肉", "蛋白质", 110, 4.0, 0, 120));
        m.put("鱼肉",   f("鱼肉",   "蛋白质", 95, 1.5, 0, 100));
        m.put("虾",     f("虾",     "蛋白质", 85, 0.6, 0,  90));
        m.put("牛肉",   f("牛肉",   "蛋白质", 120, 3.5, 0, 125));
        m.put("核桃",   f("核桃",   "坚果", 25, 2.5, 0.0, 170));
        m.put("杏仁",   f("杏仁",   "坚果", 30, 2.0, 0.0, 160));
        m.put("奇亚籽", f("奇亚籽", "碳水", 40, 1.5, 0.0, 140));
        m.put("藜麦",   f("藜麦",   "碳水", 15, 1.2, 0.0, 120));
        m.put("荞麦",   f("荞麦",   "碳水", 20, 1.5, 0.0, 140));
        foodRepo.saveAll(m.values());
        // Refresh with persisted entities
        Map<String, FoodLibrary> saved = new LinkedHashMap<>();
        m.forEach((k, v) -> saved.put(k, foodRepo.findByName(k).orElse(v)));
        return saved;
    }

    private void seedMealPlans(Map<String, FoodLibrary> f) {
        Map<FoodLibrary,String> m;
        // Breakfast
        m = Map.of(f.get("燕麦"),"燕麦洗净，加水煮开后小火煮10分钟",f.get("南瓜"),"南瓜去皮切块，一起煮至软烂",f.get("牛奶"),"最后加入牛奶拌匀即可");
        plan("BREAKFAST","燕麦南瓜牛奶粥","低嘌呤早餐，β-葡聚糖降胆固醇",
                Map.of(f.get("燕麦"),50.0, f.get("南瓜"),100.0, f.get("牛奶"),150.0), m);
        m = Map.of(f.get("大米"),"大米洗净加水煮成稀饭",f.get("鸡蛋"),"鸡蛋水煮10分钟",f.get("白菜"),"白菜洗净切段，焯水1分钟");
        plan("BREAKFAST","白粥水煮蛋配白菜","清淡易消化，保护肾脏",
                Map.of(f.get("大米"),60.0, f.get("鸡蛋"),60.0, f.get("白菜"),100.0), m);
        m = Map.of(f.get("全麦面包"),"直接食用即可",f.get("牛奶"),"牛奶加热至温热",f.get("苹果"),"苹果洗净切块");
        plan("BREAKFAST","全麦面包配牛奶苹果","粗粮+优质蛋白，稳血糖",
                Map.of(f.get("全麦面包"),80.0, f.get("牛奶"),200.0, f.get("苹果"),100.0), m);
        // Lunch
        m = Map.of(f.get("大米"),"大米洗净煮成米饭",f.get("西兰花"),"西兰花切小朵，沸水焯2分钟",f.get("胡萝卜"),"胡萝卜切片，可炒或焯水",f.get("豆腐"),"豆腐切块，轻微煎一下");
        plan("LUNCH","清炒时蔬豆腐米饭","低嘌呤，不升尿酸",
                Map.of(f.get("大米"),100.0, f.get("西兰花"),100.0,
                        f.get("胡萝卜"),80.0, f.get("豆腐"),80.0), m);
        m = Map.of(f.get("全麦面条"),"面条煮熟捞出",f.get("番茄"),"番茄切块炒出汁",f.get("鸡蛋"),"鸡蛋打散炒熟或煮荷包蛋");
        plan("LUNCH","番茄鸡蛋全麦面","高番茄红素，低脂低嘌呤",
                Map.of(f.get("全麦面条"),100.0, f.get("番茄"),150.0, f.get("鸡蛋"),60.0), m);
        m = Map.of(f.get("大米"),"大米洗净煮成米饭",f.get("冬瓜"),"冬瓜去皮切块煮汤",f.get("豆腐"),"豆腐切块放入汤中");
        plan("LUNCH","冬瓜豆腐汤配米饭","冬瓜利水，有助尿酸排泄",
                Map.of(f.get("大米"),100.0, f.get("冬瓜"),150.0, f.get("豆腐"),100.0), m);
        // Dinner
        m = Map.of(f.get("大米"),"大米洗净加水煮稀饭",f.get("南瓜"),"南瓜去皮切块一起煮至软烂",f.get("芹菜"),"芹菜洗净切段，最后放入");
        plan("DINNER","南瓜稀饭","清淡晚餐，利肝脏修复",
                Map.of(f.get("大米"),60.0, f.get("南瓜"),120.0, f.get("芹菜"),80.0), m);
        m = Map.of(f.get("鸡蛋"),"鸡蛋蒸熟或煮荷包蛋",f.get("白菜"),"白菜焯水1分钟",f.get("大米"),"大米煮成稀饭");
        plan("DINNER","蒸蛋配焯水蔬菜","最低负担晚餐，肝功异常期适用",
                Map.of(f.get("鸡蛋"),80.0, f.get("白菜"),150.0, f.get("大米"),50.0), m);
        m = Map.of(f.get("豆腐"),"豆腐切块",f.get("番茄"),"番茄切块煮汤",f.get("菠菜"),"菠菜洗净，最后放入汤中烫熟");
        plan("DINNER","番茄豆腐菠菜汤","维C促进尿酸溶解",
                Map.of(f.get("豆腐"),80.0, f.get("番茄"),100.0, f.get("菠菜"),80.0), m);
        // Breakfast — 新增
        m = new java.util.LinkedHashMap<>();
        m.put(f.get("小米"),"小米淘洗干净，加8倍水大火煮开，转小火煮20分钟至粘稠");
        m.put(f.get("南瓜"),"南瓜切小块，与小米同煮至软烂");
        m.put(f.get("鸡蛋"),"另起锅水煮鸡蛋8分钟，剥壳食用");
        plan("BREAKFAST","小米南瓜粥配水煮蛋","小米养胃，南瓜低嘌呤，早餐营养均衡",
                map2(f,"小米",80.0,"南瓜",100.0,"鸡蛋",55.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("薏米"),"薏米提前浸泡4小时，与大米一同入锅");
        m.put(f.get("大米"),"大米与薏米比例2:1，加水煮至软烂");
        m.put(f.get("苹果"),"苹果洗净切片，粥出锅后搭配食用");
        plan("BREAKFAST","薏米大米粥配苹果","薏米利湿消肿，辅助降尿酸，搭配低果糖苹果",
                map2(f,"薏米",30.0,"大米",60.0,"苹果",120.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("山药"),"山药去皮切段，上锅蒸15分钟至熟软");
        m.put(f.get("牛奶"),"牛奶加热至65°C，与山药泥混合搅打成糊");
        m.put(f.get("蓝莓"),"蓝莓洗净，铺于山药糊上点缀");
        plan("BREAKFAST","山药牛奶糊配蓝莓","山药健脾养胃，低嘌呤，餐后血糖平稳",
                map2(f,"山药",150.0,"牛奶",150.0,"蓝莓",50.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("糙米"),"糙米提前浸泡2小时，加水蒸饭或煮成稀粥");
        m.put(f.get("番茄"),"番茄切块，煮熟后与粥混合");
        m.put(f.get("鸡蛋"),"鸡蛋打散，粥快熟时倒入搅拌成蛋花");
        plan("BREAKFAST","糙米番茄蛋花粥","糙米富含B族维生素，番茄补充维C促进尿酸溶解",
                map2(f,"糙米",70.0,"番茄",100.0,"鸡蛋",55.0), m);

        // Lunch — 新增
        m = new java.util.LinkedHashMap<>();
        m.put(f.get("大米"),"大米洗净，加水蒸成米饭");
        m.put(f.get("西葫芦"),"西葫芦洗净切片，热锅少油翻炒2分钟");
        m.put(f.get("鸡蛋"),"鸡蛋打散，与西葫芦同炒，加盐调味");
        m.put(f.get("番茄"),"番茄切块，另起小锅加少许盐煮成番茄汤");
        plan("LUNCH","西葫芦炒蛋配米饭番茄汤","清淡低嘌呤，维生素丰富，一菜一汤",
                map2(f,"大米",100.0,"西葫芦",120.0,"鸡蛋",55.0,"番茄",100.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("糙米"),"糙米提前浸泡，蒸成米饭");
        m.put(f.get("茄子"),"茄子切条，蒸10分钟至软，用蒜末、生抽、醋拌匀");
        m.put(f.get("木耳"),"木耳泡发，焯水2分钟，与茄子同拌");
        m.put(f.get("番茄"),"番茄切块，加少许盐和白糖腌5分钟");
        plan("LUNCH","蒜泥茄子木耳配糙米饭","茄子软化血管，木耳降血脂，全凉拌低油",
                map2(f,"糙米",100.0,"茄子",150.0,"木耳",30.0,"番茄",80.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("山药"),"山药去皮切片，焯水1分钟");
        m.put(f.get("木耳"),"木耳泡发，焯水备用");
        m.put(f.get("胡萝卜"),"胡萝卜切片，与山药、木耳一起热锅少油翻炒3分钟");
        m.put(f.get("大米"),"大米煮成米饭");
        plan("LUNCH","山药木耳胡萝卜炒配米饭","山药滋阴，木耳降脂，低嘌呤健脾套餐",
                map2(f,"山药",120.0,"木耳",30.0,"胡萝卜",80.0,"大米",100.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("全麦面条"),"全麦面条煮熟，过凉水捞出");
        m.put(f.get("黄瓜"),"黄瓜擦丝，加盐腌5分钟，挤去水分");
        m.put(f.get("鸡蛋"),"鸡蛋煮熟切瓣");
        m.put(f.get("番茄"),"番茄切块，与面条、黄瓜、鸡蛋混合，淋少许麻油和醋");
        plan("LUNCH","凉拌全麦面","夏日爽口，低嘌呤高膳食纤维，维C促进尿酸代谢",
                map2(f,"全麦面条",100.0,"黄瓜",100.0,"鸡蛋",55.0,"番茄",80.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("红薯"),"红薯洗净，上锅蒸20分钟至软烂");
        m.put(f.get("西兰花"),"西兰花切小朵，沸水焯2分钟，捞出备用");
        m.put(f.get("豆腐"),"北豆腐切块，热锅不放油，两面煎至微黄");
        m.put(f.get("胡萝卜"),"胡萝卜切丁，与豆腐一起加水炖5分钟");
        plan("LUNCH","蒸红薯配煎豆腐西兰花","红薯替代主食，低脂饱腹，豆腐补充蛋白质",
                map2(f,"红薯",150.0,"西兰花",100.0,"豆腐",100.0,"胡萝卜",60.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("大米"),"大米煮成米饭");
        m.put(f.get("莲藕"),"莲藕去皮切片，沸水焯3分钟");
        m.put(f.get("洋葱"),"洋葱切丝，与莲藕片热锅少油翻炒2分钟，加生抽调味");
        m.put(f.get("鸡蛋"),"鸡蛋炒散，加入莲藕洋葱同炒均匀");
        plan("LUNCH","洋葱莲藕炒蛋配米饭","洋葱降脂降糖，莲藕清热凉血，低嘌呤鲜香炒菜",
                map2(f,"大米",100.0,"莲藕",120.0,"洋葱",80.0,"鸡蛋",55.0), m);

        // Dinner — 新增
        m = new java.util.LinkedHashMap<>();
        m.put(f.get("小米"),"小米洗净，加10倍水大火烧开后转小火煮25分钟");
        m.put(f.get("山药"),"山药去皮切小丁，小米煮15分钟后加入同煮");
        m.put(f.get("胡萝卜"),"胡萝卜切小丁，与山药同时加入");
        plan("DINNER","小米山药胡萝卜粥","清淡养胃，助消化，晚餐最佳选择",
                map2(f,"小米",60.0,"山药",100.0,"胡萝卜",60.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("海带"),"海带泡发切丝，冷水下锅");
        m.put(f.get("豆腐"),"豆腐切块，与海带一同入锅");
        m.put(f.get("冬瓜"),"冬瓜去皮切厚块，加入锅中，加盐煮沸后转小火煮15分钟");
        m.put(f.get("大米"),"大米煮成少量米饭（50g）配汤食用");
        plan("DINNER","海带冬瓜豆腐清汤","海带富含碘和多糖，冬瓜利水消肿，三低汤品",
                map2(f,"海带",30.0,"豆腐",80.0,"冬瓜",150.0,"大米",50.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("丝瓜"),"丝瓜去皮切段，热锅少油翻炒");
        m.put(f.get("鸡蛋"),"鸡蛋打散，倒入锅中与丝瓜同炒，加盐出锅");
        m.put(f.get("大米"),"大米煮成稀粥，与炒菜搭配");
        plan("DINNER","丝瓜炒蛋配白粥","丝瓜清热，含皂苷助降脂，低嘌呤轻盈晚餐",
                map2(f,"丝瓜",150.0,"鸡蛋",55.0,"大米",50.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("红薯"),"红薯切块，蒸熟作为主食");
        m.put(f.get("菠菜"),"菠菜焯水1分钟，挤去水分，加蒜末和少许盐拌匀");
        m.put(f.get("番茄"),"番茄切块，加少许橄榄油和盐腌制，与菠菜同盘");
        plan("DINNER","蒸红薯配蒜拌菠菜番茄","高纤维低脂晚餐，菠菜补铁，番茄促尿酸溶解",
                map2(f,"红薯",150.0,"菠菜",120.0,"番茄",80.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("薏米"),"薏米浸泡后，加大量水煮成清汤");
        m.put(f.get("冬瓜"),"冬瓜去皮切块，加入薏米汤中煮20分钟");
        m.put(f.get("芹菜"),"芹菜切段，关火前5分钟放入，保留脆嫩口感");
        plan("DINNER","薏米冬瓜芹菜汤","利湿排浊，降低血清尿酸，肾脏负担最轻的晚餐",
                map2(f,"薏米",30.0,"冬瓜",200.0,"芹菜",80.0), m);

        // Snack
        m = Map.of(f.get("苹果"),"苹果洗净直接食用或切块");
        plan("SNACK","苹果","低嘌呤水果",Map.of(f.get("苹果"),150.0), m);
        m = Map.of(f.get("蓝莓"),"蓝莓洗净直接食用");
        plan("SNACK","蓝莓","抗氧化，低嘌呤低果糖",Map.of(f.get("蓝莓"),100.0), m);
        m = Map.of(f.get("牛奶"),"牛奶加热至温热饮用");
        plan("SNACK","牛奶","优质蛋白，促尿酸排泄",Map.of(f.get("牛奶"),200.0), m);
        m = Map.of(f.get("无糖酸奶"),"无糖酸奶从冰箱取出，回温5分钟后直接食用");
        plan("SNACK","无糖酸奶","益生菌调节肠道，低糖优质蛋白",Map.of(f.get("无糖酸奶"),150.0), m);
        m = Map.of(f.get("草莓"),"草莓洗净控水，直接食用");
        plan("SNACK","草莓","维C超高，低嘌呤低果糖，助尿酸代谢",Map.of(f.get("草莓"),150.0), m);
        m = Map.of(f.get("梨"),"梨洗净去核切块食用，或榨汁不过滤直接饮用");
        plan("SNACK","梨","润肺清热，低嘌呤，含较少果糖",Map.of(f.get("梨"),150.0), m);

        // 新增食谱
        m = new java.util.LinkedHashMap<>();
        m.put(f.get("藜麦"),"藜麦提前浸泡1小时，冲洗后加水煮15分钟至发芽圈出现");
        m.put(f.get("番茄"),"番茄用开水烫后去皮，切块煮成酱汁");
        m.put(f.get("鸡蛋"),"鸡蛋打散，倒入番茄酱汁中做成番茄炒蛋");
        m.put(f.get("菠菜"),"菠菜焯水后铺在盘底");
        plan("BREAKFAST","藜麦番茄炒蛋配菠菜","藜麦完全蛋白，番茄维C促进尿酸代谢，菠菜补铁",
                map2(f,"藜麦",80.0,"番茄",100.0,"鸡蛋",55.0,"菠菜",80.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("荞麦"),"荞麦面加水揉成面团，擀成面条或切成猫耳面");
        m.put(f.get("黄瓜"),"黄瓜切丝，用少许盐腌制挤水");
        m.put(f.get("鸡胸肉"),"鸡胸肉水煮后撕成丝");
        m.put(f.get("紫菜"),"紫菜剪碎，与鸡丝、黄瓜丝拌入面中");
        plan("BREAKFAST","荞麦面鸡丝凉拌","高蛋白低脂，荞麦黄酮护血管，夏季开胃",
                map2(f,"荞麦",100.0,"黄瓜",80.0,"鸡胸肉",80.0,"紫菜",10.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("燕麦"),"燕麦加水煮成稠粥");
        m.put(f.get("奇亚籽"),"奇亚籽提前泡发，加入燕麦粥中增加口感");
        m.put(f.get("核桃"),"核桃砸碎，撒在粥上");
        m.put(f.get("蓝莓"),"蓝莓洗净后与燕麦粥拌匀");
        plan("BREAKFAST","奇亚籽燕麦核桃蓝莓碗","高Omega-3，抗炎低嘌呤，肠道健康",
                map2(f,"燕麦",60.0,"奇亚籽",15.0,"核桃",20.0,"蓝莓",60.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("小米"),"小米加水煮成稀粥");
        m.put(f.get("南瓜"),"南瓜蒸熟后压成泥，加入小米粥中");
        m.put(f.get("红薯"),"红薯切块蒸熟，粥快好时加入");
        plan("BREAKFAST","小米南瓜红薯粥","三重碳水搭配，粗粮纤维丰富，餐后血糖平稳",
                map2(f,"小米",50.0,"南瓜",80.0,"红薯",80.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("全麦面包"),"全麦面包烤至微脆");
        m.put(f.get("鸡蛋"),"鸡蛋蒸成嫩蛋羹");
        m.put(f.get("番茄"),"番茄切片，用少许橄榄油轻煎");
        m.put(f.get("杏仁"),"杏仁浸泡后切碎，撒在蛋羹上");
        plan("BREAKFAST","烤全麦面包配嫩蛋羹煎番茄","高蛋白低碳水，杏仁含维E护细胞",
                map2(f,"全麦面包",60.0,"鸡蛋",80.0,"番茄",80.0,"杏仁",15.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("糙米"),"糙米浸泡后蒸成米饭");
        m.put(f.get("香菇"),"香菇泡发后切片，与糙米同蒸");
        m.put(f.get("胡萝卜"),"胡萝卜切丁，与香菇糙米同煮");
        m.put(f.get("豆腐"),"豆腐切块，蒸熟后淋少许生抽");
        plan("LUNCH","香菇胡萝卜糙米饭配蒸豆腐","香菇多糖提升免疫，糙米缓释血糖，豆腐优质蛋白",
                map2(f,"糙米",100.0,"香菇",50.0,"胡萝卜",60.0,"豆腐",100.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("荞麦"),"荞麦面加水和成面团，擀成面条");
        m.put(f.get("金针菇"),"金针菇去根洗净，焯水2分钟");
        m.put(f.get("鸡蛋"),"鸡蛋煮熟切块或做成蛋丝");
        m.put(f.get("黄瓜"),"黄瓜切丝，与金针菇、蛋丝铺在面条上");
        plan("LUNCH","荞麦凉面金针菇黄瓜","金针菇含多糖提升免疫，凉面低脂适合夏季",
                map2(f,"荞麦",100.0,"金针菇",80.0,"鸡蛋",55.0,"黄瓜",60.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("大米"),"大米煮成米饭");
        m.put(f.get("鱼肉"),"鱼肉切片，用少许盐和姜丝腌制10分钟，清蒸8分钟");
        m.put(f.get("西兰花"),"西兰花切小朵，焯水2分钟保持翠绿");
        m.put(f.get("银耳"),"银耳泡发后煮成银耳羹作为汤品");
        plan("LUNCH","清蒸鱼肉配西兰花银耳羹","高蛋白低脂，鱼肉Omega-3抗炎，银耳润肺",
                map2(f,"大米",100.0,"鱼肉",120.0,"西兰花",100.0,"银耳",30.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("紫菜"),"紫菜剪碎，与米饭、海苔碎捏成饭团");
        m.put(f.get("黄瓜"),"黄瓜切片，包裹在饭团外或单独配餐");
        m.put(f.get("鸡蛋"),"鸡蛋做成厚蛋烧，切条备用");
        m.put(f.get("藜麦"),"藜麦煮熟后作为底饭，提供高蛋白");
        plan("LUNCH","紫菜藜麦饭团配厚蛋烧","高蛋白饭团，厚蛋烧增加早餐感，携带方便",
                map2(f,"紫菜",10.0,"黄瓜",60.0,"鸡蛋",80.0,"藜麦",60.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("土豆"),"土豆去皮切块，蒸熟后压成泥");
        m.put(f.get("鸡胸肉"),"鸡胸肉切丝，用少许生抽和料酒腌制，热锅少油翻炒");
        m.put(f.get("胡萝卜"),"胡萝卜切丁，与鸡丝一起翻炒后盖在土豆泥上");
        m.put(f.get("西兰花"),"西兰花焯水后摆盘装饰");
        plan("LUNCH","土豆泥鸡丝胡萝卜","土豆替代米饭减少嘌呤，鸡丝高蛋白，摆盘精美",
                map2(f,"土豆",200.0,"鸡胸肉",80.0,"胡萝卜",60.0,"西兰花",60.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("全麦面条"),"全麦面条煮熟，过冷水沥干");
        m.put(f.get("番茄"),"番茄切块，小火慢煮成浓稠番茄酱汁");
        m.put(f.get("牛肉"),"牛肉切薄片，用少许淀粉抓匀，番茄酱汁中滑熟");
        m.put(f.get("菠菜"),"菠菜洗净，番茄牛肉面煮好后撒在面上");
        plan("LUNCH","番茄牛肉全麦面","番茄红素丰富，牛肉补铁，番茄酱汁酸甜开胃",
                map2(f,"全麦面条",100.0,"番茄",150.0,"牛肉",80.0,"菠菜",60.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("燕麦"),"燕麦加水煮成稠粥");
        m.put(f.get("南瓜"),"南瓜切小块，与燕麦同煮至软烂");
        m.put(f.get("虾"),"虾去壳去虾线，用少许姜丝和盐煮熟");
        plan("DINNER","燕麦南瓜鲜虾粥","燕麦β-葡聚糖清血管，南瓜护肾，虾高蛋白低脂",
                map2(f,"燕麦",50.0,"南瓜",100.0,"虾",80.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("小米"),"小米加水煮成稀粥");
        m.put(f.get("鸡腿肉"),"鸡腿肉去皮切块，小火炖煮30分钟后捞出");
        m.put(f.get("香菇"),"香菇泡发切片，与鸡肉一起在粥中炖煮");
        m.put(f.get("胡萝卜"),"胡萝卜切丁，最后放入煮5分钟");
        plan("DINNER","香菇鸡肉小米粥","鸡肉温中补气，香菇助消化，晚餐暖胃首选",
                map2(f,"小米",50.0,"鸡腿肉",80.0,"香菇",40.0,"胡萝卜",50.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("糙米"),"糙米浸泡后蒸成软饭");
        m.put(f.get("金针菇"),"金针菇去根，与少许生抽炒熟");
        m.put(f.get("鸡蛋"),"鸡蛋打散，与金针菇同炒成金针菇滑蛋");
        m.put(f.get("紫菜"),"紫菜剪碎，沸水冲泡成紫菜汤");
        plan("DINNER","金针菇滑蛋糙米饭配紫菜汤","金针菇健脑益智，滑蛋嫩滑可口，紫菜汤补碘",
                map2(f,"糙米",80.0,"金针菇",100.0,"鸡蛋",60.0,"紫菜",8.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("荞麦"),"荞麦面加水和成面团，擀成薄饼");
        m.put(f.get("菠菜"),"菠菜焯水后切碎，与鸡蛋混合做成菠菜鸡蛋饼馅");
        m.put(f.get("鸡蛋"),"鸡蛋打散，煎成薄蛋饼");
        plan("DINNER","荞麦菠菜蛋饼","高纤维低嘌呤，饼皮无油，晚餐轻食首选",
                map2(f,"荞麦",80.0,"菠菜",100.0,"鸡蛋",80.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("土豆"),"土豆切丝，用清水浸泡去淀粉");
        m.put(f.get("紫菜"),"紫菜剪碎，沸水泡成汤底");
        m.put(f.get("鸡蛋"),"鸡蛋打散，倒入紫菜汤中成蛋花");
        m.put(f.get("银耳"),"银耳泡发后撕成小朵，与紫菜蛋花汤同煮");
        plan("DINNER","土豆丝凉拌配紫菜银耳蛋花汤","土豆丝清爽解腻，银耳润燥，蛋花汤暖身",
                map2(f,"土豆",150.0,"紫菜",8.0,"鸡蛋",55.0,"银耳",25.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("奇亚籽"),"奇亚籽加水泡发成凝胶状");
        m.put(f.get("藜麦"),"藜麦煮熟后放凉");
        m.put(f.get("蓝莓"),"蓝莓洗净，与奇亚籽、藜麦混合");
        m.put(f.get("杏仁"),"杏仁切碎，撒在碗中");
        plan("SNACK","奇亚籽藜麦蓝莓能量碗","高蛋白高纤维，Omega-3丰富，抗氧化下午茶",
                map2(f,"奇亚籽",20.0,"藜麦",40.0,"蓝莓",80.0,"杏仁",15.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("核桃"),"核桃仁直接食用或微微烘烤");
        m.put(f.get("苹果"),"苹果洗净切块，搭配核桃一起吃");
        m.put(f.get("鸡蛋"),"鸡蛋水煮或做成溏心蛋");
        plan("SNACK","核桃苹果水煮蛋","核桃补脑，苹果纤维助消化，水煮蛋优质蛋白",
                map2(f,"核桃",30.0,"苹果",120.0,"鸡蛋",55.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("藜麦"),"藜麦煮熟后放入保温杯");
        m.put(f.get("紫菜"),"紫菜剪碎，加入保温杯");
        m.put(f.get("金针菇"),"金针菇去根焯水，加入保温杯");
        plan("SNACK","藜麦紫菜金针菇温沙拉","温热沙拉保护肠胃，藜麦高蛋白，下午饱腹感强",
                map2(f,"藜麦",50.0,"紫菜",5.0,"金针菇",60.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("杏仁"),"杏仁烘烤后捣碎");
        m.put(f.get("燕麦"),"燕麦煮成稠粥，放凉后加入杏仁碎");
        m.put(f.get("草莓"),"草莓洗净切块，铺在燕麦杏仁粥上");
        plan("SNACK","杏仁燕麦草莓杯","杏仁维E护肤，燕麦缓释能量，草莓促尿酸溶解",
                map2(f,"杏仁",20.0,"燕麦",40.0,"草莓",100.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("银耳"),"银耳泡发后撕成小朵");
        m.put(f.get("紫菜"),"紫菜剪碎，与银耳同煮成汤");
        m.put(f.get("鸡蛋"),"鸡蛋打散，倒入汤中形成蛋花");
        plan("SNACK","银耳紫菜蛋花汤","润肺清燥，银耳胶质养颜，宵夜轻食无负担",
                map2(f,"银耳",20.0,"紫菜",5.0,"鸡蛋",40.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("红薯"),"红薯蒸熟后切成条，烤箱烤脆或平底锅煎脆");
        m.put(f.get("鸡胸肉"),"鸡胸肉水煮后撕成鸡丝");
        m.put(f.get("胡萝卜"),"胡萝卜切丝，与鸡丝拌匀");
        plan("SNACK","红薯脆条配鸡丝胡萝卜","红薯替代薯片，鸡丝高蛋白，健康零食首选",
                map2(f,"红薯",100.0,"鸡胸肉",60.0,"胡萝卜",50.0), m);

        m = new java.util.LinkedHashMap<>();
        m.put(f.get("香菇"),"香菇去蒂，整个焯水后切片");
        m.put(f.get("土豆"),"土豆切块，与香菇同蒸后压成泥");
        m.put(f.get("奇亚籽"),"奇亚籽泡发，撒在土豆香菇泥上");
        plan("SNACK","香菇土豆奇亚籽泥","香菇增鲜，土豆饱腹，奇亚籽高纤维",
                map2(f,"香菇",50.0,"土豆",120.0,"奇亚籽",10.0), m);
    }

    private void plan(String mealType, String name, String desc, Map<FoodLibrary,Double> items, Map<FoodLibrary,String> methods) {
        MealPlan p = new MealPlan();
        p.setMealType(mealType); p.setPlanName(name); p.setDescription(desc);
        p = planRepo.save(p);
        for (var e : items.entrySet()) {
            MealPlanItem item = new MealPlanItem();
            item.setPlan(p); item.setFood(e.getKey());
            item.setAmountG(BigDecimal.valueOf(e.getValue()));
            item.setCookingMethod(methods.get(e.getKey()));
            p.getItems().add(item);
        }
        planRepo.save(p);
    }

    /** Build a Map<FoodLibrary, Double> from alternating key/value pairs, bypassing Map.of's 10-entry limit. */
    @SuppressWarnings("unchecked")
    private Map<FoodLibrary, Double> map2(Map<String, FoodLibrary> foods, Object... pairs) {
        Map<FoodLibrary, Double> result = new LinkedHashMap<>();
        for (int i = 0; i < pairs.length; i += 2) {
            result.put(foods.get((String) pairs[i]), (Double) pairs[i + 1]);
        }
        return result;
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
