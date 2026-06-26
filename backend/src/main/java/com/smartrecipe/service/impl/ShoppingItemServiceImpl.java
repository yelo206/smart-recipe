package com.smartrecipe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartrecipe.dto.ShoppingItemDTO;
import com.smartrecipe.entity.*;
import com.smartrecipe.exception.BusinessException;
import com.smartrecipe.mapper.*;
import com.smartrecipe.service.ShoppingItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 购物清单服务实现类
 */
@Service
public class ShoppingItemServiceImpl implements ShoppingItemService {

    @Autowired
    private ShoppingItemMapper shoppingItemMapper;
    @Autowired
    private MealPlanMapper mealPlanMapper;
    @Autowired
    private RecipeIngredientMapper recipeIngredientMapper;
    @Autowired
    private IngredientMapper ingredientMapper;

    @Override
    public List<ShoppingItem> getItemsByUserId(Long userId) {
        return shoppingItemMapper.selectList(
                new LambdaQueryWrapper<ShoppingItem>()
                        .eq(ShoppingItem::getUserId, userId)
                        .orderByAsc(ShoppingItem::getPurchased)
                        .orderByDesc(ShoppingItem::getCreateTime)
        );
    }

    @Override
    public Long addItem(ShoppingItemDTO itemDTO, Long userId) {
        ShoppingItem item = new ShoppingItem();
        BeanUtils.copyProperties(itemDTO, item);
        item.setUserId(userId);
        if (item.getPurchased() == null) {
            item.setPurchased(0);
        }
        shoppingItemMapper.insert(item);
        return item.getId();
    }

    @Override
    public void updateItem(ShoppingItemDTO itemDTO) {
        if (itemDTO.getId() == null) {
            throw new BusinessException(400, "清单项ID不能为空");
        }
        ShoppingItem item = new ShoppingItem();
        BeanUtils.copyProperties(itemDTO, item);
        shoppingItemMapper.updateById(item);
    }

    @Override
    public void deleteItem(Long id) {
        shoppingItemMapper.deleteById(id);
    }

    @Override
    public void togglePurchased(Long id) {
        ShoppingItem item = shoppingItemMapper.selectById(id);
        if (item == null) {
            throw new BusinessException(404, "清单项不存在");
        }
        item.setPurchased(item.getPurchased() == 0 ? 1 : 0);
        shoppingItemMapper.updateById(item);
    }

    @Override
    @Transactional
    public void generateFromMealPlan(Long userId, LocalDate startDate, LocalDate endDate) {
        // 获取日期范围内的膳食计划
        List<MealPlan> plans = mealPlanMapper.selectList(
                new LambdaQueryWrapper<MealPlan>()
                        .eq(MealPlan::getUserId, userId)
                        .between(MealPlan::getPlanDate, startDate, endDate)
        );

        if (plans.isEmpty()) {
            throw new BusinessException(400, "指定日期范围内无膳食计划");
        }

        // 收集所有菜谱ID
        List<Long> recipeIds = plans.stream().map(MealPlan::getRecipeId).toList();

        // 查询所有菜谱的食材
        List<RecipeIngredient> recipeIngredients = recipeIngredientMapper.selectList(
                new LambdaQueryWrapper<RecipeIngredient>()
                        .in(RecipeIngredient::getRecipeId, recipeIds)
        );

        // 获取食材详情
        List<Long> ingredientIds = recipeIngredients.stream()
                .map(RecipeIngredient::getIngredientId).distinct().toList();
        Map<Long, Ingredient> ingredientMap = ingredientMapper.selectBatchIds(ingredientIds)
                .stream().collect(Collectors.toMap(Ingredient::getId, i -> i));

        // 合并同种食材并生成购物清单
        Map<Long, ShoppingItemDTO> mergedItems = new HashMap<>();
        for (RecipeIngredient ri : recipeIngredients) {
            Ingredient ingredient = ingredientMap.get(ri.getIngredientId());
            if (ingredient == null) continue;

            ShoppingItemDTO existing = mergedItems.get(ri.getIngredientId());
            if (existing != null) {
                existing.setAmount(existing.getAmount().add(ri.getAmount()));
            } else {
                ShoppingItemDTO item = new ShoppingItemDTO();
                item.setIngredientId(ri.getIngredientId());
                item.setName(ingredient.getName());
                item.setAmount(ri.getAmount());
                item.setUnit(ri.getUnit() != null ? ri.getUnit() : ingredient.getUnit());
                item.setPurchased(0);
                mergedItems.put(ri.getIngredientId(), item);
            }
        }

        // 批量插入
        for (ShoppingItemDTO item : mergedItems.values()) {
            addItem(item, userId);
        }
    }
}
