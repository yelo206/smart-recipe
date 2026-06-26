package com.smartrecipe.controller;

import com.smartrecipe.dto.NutritionStatsDTO;
import com.smartrecipe.service.NutritionLogService;
import com.smartrecipe.utils.JwtUtils;
import com.smartrecipe.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

/**
 * 营养统计控制器
 */
@RestController
@RequestMapping("/nutrition")
public class NutritionLogController {

    @Autowired
    private NutritionLogService nutritionLogService;
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 获取每日营养统计
     */
    @GetMapping("/daily/{date}")
    public Result<NutritionStatsDTO> getDailyStats(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromToken(request.getHeader("Authorization").substring(7));
        return Result.success(nutritionLogService.getDailyStats(userId, date));
    }

    /**
     * 获取周营养统计
     */
    @GetMapping("/weekly")
    public Result<NutritionStatsDTO> getWeeklyStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromToken(request.getHeader("Authorization").substring(7));
        return Result.success(nutritionLogService.getWeeklyStats(userId, startDate));
    }
}
