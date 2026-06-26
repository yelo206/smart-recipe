package com.smartrecipe.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 购物清单项请求DTO
 */
@Data
public class ShoppingItemDTO {
    private Long id;
    private Long ingredientId;
    private String name;
    private BigDecimal amount;
    private String unit;
    private Integer purchased;
}
