package com.smartrecipe.dto;

import lombok.Data;
import java.time.LocalDate;

/**
 * 膳食计划请求DTO
 */
@Data
public class MealPlanDTO {
    private Long id;
    private Long recipeId;
    private LocalDate planDate;
    private Integer mealType;
    private String note;
}
