package com.smartrecipe.service;

import com.smartrecipe.dto.ShoppingItemDTO;
import com.smartrecipe.entity.ShoppingItem;
import java.time.LocalDate;
import java.util.List;

/**
 * 购物清单服务接口
 */
public interface ShoppingItemService {
    List<ShoppingItem> getItemsByUserId(Long userId);
    Long addItem(ShoppingItemDTO itemDTO, Long userId);
    void updateItem(ShoppingItemDTO itemDTO);
    void deleteItem(Long id);
    void togglePurchased(Long id);
    /** 根据膳食计划生成购物清单 */
    void generateFromMealPlan(Long userId, LocalDate startDate, LocalDate endDate);
}
