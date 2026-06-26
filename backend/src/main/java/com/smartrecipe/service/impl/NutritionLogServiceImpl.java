package com.smartrecipe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartrecipe.dto.NutritionStatsDTO;
import com.smartrecipe.entity.*;
import com.smartrecipe.mapper.*;
import com.smartrecipe.service.NutritionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 营养记录服务实现类
 */
@Service
public class NutritionLogServiceImpl implements NutritionLogService {

    @Autowired
    private NutritionLogMapper nutritionLogMapper;
    @Autowired
    private MealPlanMapper mealPlanMapper;
    @Autowired
    private RecipeMapper recipeMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public NutritionStatsDTO getDailyStats(Long userId, LocalDate date) {
        List<MealPlan> plans = mealPlanMapper.selectList(
                new LambdaQueryWrapper<MealPlan>()
                        .eq(MealPlan::getUserId, userId)
                        .eq(MealPlan::getPlanDate, date)
        );

        User user = userMapper.selectById(userId);
        int calorieGoal = user != null && user.getDailyCalorieGoal() != null ? user.getDailyCalorieGoal() : 2000;

        int totalCalories = 0;
        double totalProtein = 0;
        double totalFat = 0;
        double totalCarb = 0;

        for (MealPlan plan : plans) {
            Recipe recipe = recipeMapper.selectById(plan.getRecipeId());
            if (recipe != null) {
                totalCalories += recipe.getCalories() != null ? recipe.getCalories() : 0;
                totalProtein += recipe.getProtein() != null ? recipe.getProtein().doubleValue() : 0;
                totalFat += recipe.getFat() != null ? recipe.getFat().doubleValue() : 0;
                totalCarb += recipe.getCarbohydrate() != null ? recipe.getCarbohydrate().doubleValue() : 0;
            }
        }

        NutritionStatsDTO stats = new NutritionStatsDTO();
        stats.setDate(date);
        stats.setTotalCalories(totalCalories);
        stats.setTotalProtein(totalProtein);
        stats.setTotalFat(totalFat);
        stats.setTotalCarb(totalCarb);
        stats.setCalorieGoal(calorieGoal);
        stats.setCaloriePercentage(calorieGoal > 0 ? (double) totalCalories / calorieGoal * 100 : 0);
        return stats;
    }

    @Override
    public NutritionStatsDTO getWeeklyStats(Long userId, LocalDate startDate) {
        NutritionStatsDTO stats = getDailyStats(userId, startDate);
        List<NutritionStatsDTO.DailyCalorie> weeklyCalories = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = startDate.minusDays(i);
            NutritionStatsDTO daily = getDailyStats(userId, date);
            NutritionStatsDTO.DailyCalorie dc = new NutritionStatsDTO.DailyCalorie();
            dc.setDate(date);
            dc.setCalories(daily.getTotalCalories());
            weeklyCalories.add(dc);
        }
        stats.setWeeklyCalories(weeklyCalories);
        return stats;
    }

    @Override
    public void updateDailyLog(Long userId, LocalDate date) {
        NutritionStatsDTO stats = getDailyStats(userId, date);
        
        NutritionLog existing = nutritionLogMapper.selectOne(
                new LambdaQueryWrapper<NutritionLog>()
                        .eq(NutritionLog::getUserId, userId)
                        .eq(NutritionLog::getLogDate, date)
        );

        if (existing != null) {
            existing.setTotalCalories(stats.getTotalCalories());
            existing.setTotalProtein(BigDecimal.valueOf(stats.getTotalProtein()));
            existing.setTotalFat(BigDecimal.valueOf(stats.getTotalFat()));
            existing.setTotalCarb(BigDecimal.valueOf(stats.getTotalCarb()));
            nutritionLogMapper.updateById(existing);
        } else {
            NutritionLog log = new NutritionLog();
            log.setUserId(userId);
            log.setLogDate(date);
            log.setTotalCalories(stats.getTotalCalories());
            log.setTotalProtein(BigDecimal.valueOf(stats.getTotalProtein()));
            log.setTotalFat(BigDecimal.valueOf(stats.getTotalFat()));
            log.setTotalCarb(BigDecimal.valueOf(stats.getTotalCarb()));
            nutritionLogMapper.insert(log);
        }
    }
}
