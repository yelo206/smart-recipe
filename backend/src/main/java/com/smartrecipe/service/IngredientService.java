package com.smartrecipe.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartrecipe.entity.Ingredient;
import java.util.List;

/**
 * 食材服务接口
 */
public interface IngredientService {
    Page<Ingredient> getIngredientPage(int current, int size, String keyword);
    Ingredient getIngredientById(Long id);
    Long createIngredient(Ingredient ingredient);
    void updateIngredient(Ingredient ingredient);
    void deleteIngredient(Long id);
    List<Ingredient> getAll();
}
