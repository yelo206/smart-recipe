package com.smartrecipe.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartrecipe.dto.RecipeDTO;
import com.smartrecipe.entity.Recipe;
import java.util.List;

/**
 * 菜谱服务接口
 */
public interface RecipeService {
    Page<Recipe> getRecipePage(int current, int size, String keyword, Long categoryId);
    Recipe getRecipeById(Long id);
    RecipeDTO getRecipeDetail(Long id);
    Long createRecipe(RecipeDTO recipeDTO, Long userId);
    void updateRecipe(RecipeDTO recipeDTO);
    void deleteRecipe(Long id);
    List<Recipe> getRecipesByUserId(Long userId);

    /** 根据食材ID列表搜索菜谱，按匹配数量降序排列 */
    List<Recipe> searchByIngredients(List<Long> ingredientIds);
}
