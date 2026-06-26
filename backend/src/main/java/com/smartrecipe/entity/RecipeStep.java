package com.smartrecipe.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 菜谱步骤实体类
 */
@Data
@TableName("recipe_steps")
public class RecipeStep {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long recipeId;

    /** 步骤序号 */
    private Integer stepNumber;

    /** 步骤描述 */
    private String description;

    /** 步骤图片 */
    private String imageUrl;
}
