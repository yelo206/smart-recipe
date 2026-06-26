package com.smartrecipe.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜谱实体类
 */
@Data
@TableName("recipes")
public class Recipe {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long categoryId;

    private String title;

    private String description;

    private String coverImage;

    /** 难度：1-简单 2-中等 3-困难 */
    private Integer difficulty;

    /** 烹饪时间（分钟） */
    private Integer cookTime;

    /** 份数（人份） */
    private Integer servings;

    /** 卡路里（千卡） */
    private Integer calories;

    /** 蛋白质（克） */
    private BigDecimal protein;

    /** 脂肪（克） */
    private BigDecimal fat;

    /** 碳水化合物（克） */
    private BigDecimal carbohydrate;

    /** 匹配食材数量（非数据库字段，用于按食材搜索时展示） */
    @TableField(exist = false)
    private Integer matchCount;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
