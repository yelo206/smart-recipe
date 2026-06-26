package com.smartrecipe.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 菜谱-食材关联实体类
 */
@Data
@TableName("recipe_ingredients")
public class RecipeIngredient {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long recipeId;

    private Long ingredientId;

    /** 用量 */
    private BigDecimal amount;

    private String unit;
}
