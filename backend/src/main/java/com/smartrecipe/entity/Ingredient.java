package com.smartrecipe.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 食材实体类
 */
@Data
@TableName("ingredients")
public class Ingredient {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    /** 计量单位（个、克、毫升等） */
    private String unit;

    /** 每单位卡路里 */
    private BigDecimal caloriesPerUnit;

    /** 每单位蛋白质 */
    private BigDecimal proteinPerUnit;

    /** 每单位脂肪 */
    private BigDecimal fatPerUnit;

    /** 每单位碳水 */
    private BigDecimal carbPerUnit;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
