package com.smartrecipe.controller;

import com.smartrecipe.dto.MealPlanDTO;
import com.smartrecipe.entity.MealPlan;
import com.smartrecipe.service.MealPlanService;
import com.smartrecipe.utils.JwtUtils;
import com.smartrecipe.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/**
 * 膳食计划控制器
 */
@RestController
@RequestMapping("/meal-plan")
public class MealPlanController {

    @Autowired
    private MealPlanService mealPlanService;
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 查询日期范围内的膳食计划
     */
    @GetMapping("/range")
    public Result<List<MealPlan>> getPlansByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromToken(request.getHeader("Authorization").substring(7));
        return Result.success(mealPlanService.getPlansByDateRange(userId, start, end));
    }

    /**
     * 查询某天的膳食计划
     */
    @GetMapping("/day/{date}")
    public Result<List<MealPlan>> getPlansByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromToken(request.getHeader("Authorization").substring(7));
        return Result.success(mealPlanService.getPlansByDate(userId, date));
    }

    @PostMapping
    public Result<Long> create(@Valid @RequestBody MealPlanDTO planDTO, HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromToken(request.getHeader("Authorization").substring(7));
        return Result.success("创建成功", mealPlanService.createPlan(planDTO, userId));
    }

    @PutMapping
    public Result<Void> update(@Valid @RequestBody MealPlanDTO planDTO) {
        mealPlanService.updatePlan(planDTO);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        mealPlanService.deletePlan(id);
        return Result.success("删除成功", null);
    }

    @PutMapping("/toggle/{id}")
    public Result<Void> toggleCompleted(@PathVariable Long id) {
        mealPlanService.toggleCompleted(id);
        return Result.success("操作成功", null);
    }
}
