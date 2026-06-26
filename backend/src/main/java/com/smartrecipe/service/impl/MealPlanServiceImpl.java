package com.smartrecipe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartrecipe.dto.MealPlanDTO;
import com.smartrecipe.entity.MealPlan;
import com.smartrecipe.exception.BusinessException;
import com.smartrecipe.mapper.MealPlanMapper;
import com.smartrecipe.service.MealPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

/**
 * 膳食计划服务实现类
 */
@Service
public class MealPlanServiceImpl implements MealPlanService {

    @Autowired
    private MealPlanMapper mealPlanMapper;

    @Override
    public List<MealPlan> getPlansByDateRange(Long userId, LocalDate start, LocalDate end) {
        return mealPlanMapper.selectList(
                new LambdaQueryWrapper<MealPlan>()
                        .eq(MealPlan::getUserId, userId)
                        .between(MealPlan::getPlanDate, start, end)
                        .orderByAsc(MealPlan::getPlanDate)
                        .orderByAsc(MealPlan::getMealType)
        );
    }

    @Override
    public List<MealPlan> getPlansByDate(Long userId, LocalDate date) {
        return mealPlanMapper.selectList(
                new LambdaQueryWrapper<MealPlan>()
                        .eq(MealPlan::getUserId, userId)
                        .eq(MealPlan::getPlanDate, date)
                        .orderByAsc(MealPlan::getMealType)
        );
    }

    @Override
    public Long createPlan(MealPlanDTO planDTO, Long userId) {
        MealPlan plan = new MealPlan();
        BeanUtils.copyProperties(planDTO, plan);
        plan.setUserId(userId);
        plan.setCompleted(0);
        mealPlanMapper.insert(plan);
        return plan.getId();
    }

    @Override
    public void updatePlan(MealPlanDTO planDTO) {
        if (planDTO.getId() == null) {
            throw new BusinessException(400, "计划ID不能为空");
        }
        MealPlan plan = new MealPlan();
        BeanUtils.copyProperties(planDTO, plan);
        mealPlanMapper.updateById(plan);
    }

    @Override
    public void deletePlan(Long id) {
        mealPlanMapper.deleteById(id);
    }

    @Override
    public void toggleCompleted(Long id) {
        MealPlan plan = mealPlanMapper.selectById(id);
        if (plan == null) {
            throw new BusinessException(404, "膳食计划不存在");
        }
        plan.setCompleted(plan.getCompleted() == 0 ? 1 : 0);
        mealPlanMapper.updateById(plan);
    }
}
