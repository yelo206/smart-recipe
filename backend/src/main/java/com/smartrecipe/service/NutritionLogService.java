package com.smartrecipe.service;

import com.smartrecipe.dto.NutritionStatsDTO;
import java.time.LocalDate;

/**
 * 营养记录服务接口
 */
public interface NutritionLogService {
    NutritionStatsDTO getDailyStats(Long userId, LocalDate date);
    NutritionStatsDTO getWeeklyStats(Long userId, LocalDate startDate);
    void updateDailyLog(Long userId, LocalDate date);
}
