package com.smartrecipe.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 膳食计划实体类
 */
@Data
@TableName("meal_plans")
public class MealPlan {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long recipeId;

    /** 计划日期 */
    private LocalDate planDate;

    /** 餐次：1-早餐 2-午餐 3-晚餐 4-加餐 */
    private Integer mealType;

    private String note;

    /** 是否完成：0-否 1-是 */
    private Integer completed;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
