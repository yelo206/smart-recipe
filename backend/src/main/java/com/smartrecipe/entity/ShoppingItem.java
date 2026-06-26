package com.smartrecipe.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 购物清单项实体类
 */
@Data
@TableName("shopping_items")
public class ShoppingItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long ingredientId;

    private String name;

    private BigDecimal amount;

    private String unit;

    /** 是否已购买：0-否 1-是 */
    private Integer purchased;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
