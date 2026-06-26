package com.smartrecipe.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 菜谱请求DTO（包含食材和步骤）
 */
@Data
public class RecipeDTO {
    private Long id;
    private Long categoryId;
    
    @NotBlank(message = "菜谱名称不能为空")
    private String title;
    
    private String description;
    private String coverImage;
    private Integer difficulty;
    private Integer cookTime;
    private Integer servings;
    private Integer calories;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal carbohydrate;
    
    /** 食材列表 */
    private List<IngredientItem> ingredients;
    
    /** 步骤列表 */
    private List<StepItem> steps;

    @Data
    public static class IngredientItem {
        private Long ingredientId;
        private String name;
        private BigDecimal amount;
        private String unit;
    }

    @Data
    public static class StepItem {
        private Integer stepNumber;
        private String description;
        private String imageUrl;
    }
}
