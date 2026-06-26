package com.smartrecipe.service;

import com.smartrecipe.dto.MealPlanDTO;
import com.smartrecipe.entity.MealPlan;
import java.time.LocalDate;
import java.util.List;

/**
 * 膳食计划服务接口
 */
public interface MealPlanService {
    List<MealPlan> getPlansByDateRange(Long userId, LocalDate start, LocalDate end);
    List<MealPlan> getPlansByDate(Long userId, LocalDate date);
    Long createPlan(MealPlanDTO planDTO, Long userId);
    void updatePlan(MealPlanDTO planDTO);
    void deletePlan(Long id);
    void toggleCompleted(Long id);
}
