package com.smartrecipe.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

/**
 * 营养统计响应DTO
 */
@Data
public class NutritionStatsDTO {
    private LocalDate date;
    private Integer totalCalories;
    private Double totalProtein;
    private Double totalFat;
    private Double totalCarb;
    private Integer calorieGoal;
    private Double caloriePercentage;

    /** 近7天每日卡路里 */
    private List<DailyCalorie> weeklyCalories;

    @Data
    public static class DailyCalorie {
        private LocalDate date;
        private Integer calories;
    }
}
