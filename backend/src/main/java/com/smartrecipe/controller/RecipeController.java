package com.smartrecipe.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartrecipe.dto.RecipeDTO;
import com.smartrecipe.entity.Recipe;
import com.smartrecipe.service.RecipeService;
import com.smartrecipe.utils.JwtUtils;
import com.smartrecipe.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 菜谱控制器
 * 处理菜谱的增删改查
 */
@RestController
@RequestMapping("/recipe")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;
    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 分页查询菜谱
     */
    @GetMapping("/page")
    public Result<Page<Recipe>> getRecipePage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId) {
        return Result.success(recipeService.getRecipePage(current, size, keyword, categoryId));
    }

    /**
     * 获取菜谱详情（含食材和步骤）
     */
    @GetMapping("/detail/{id}")
    public Result<RecipeDTO> getRecipeDetail(@PathVariable Long id) {
        return Result.success(recipeService.getRecipeDetail(id));
    }

    /**
     * 创建菜谱
     */
    @PostMapping
    public Result<Long> createRecipe(@Valid @RequestBody RecipeDTO recipeDTO, HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromToken(request.getHeader("Authorization").substring(7));
        return Result.success("创建成功", recipeService.createRecipe(recipeDTO, userId));
    }

    /**
     * 更新菜谱
     */
    @PutMapping
    public Result<Void> updateRecipe(@Valid @RequestBody RecipeDTO recipeDTO) {
        recipeService.updateRecipe(recipeDTO);
        return Result.success("更新成功", null);
    }

    /**
     * 删除菜谱
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return Result.success("删除成功", null);
    }

    /**
     * 获取我的菜谱
     */
    @GetMapping("/my")
    public Result<List<Recipe>> getMyRecipes(HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromToken(request.getHeader("Authorization").substring(7));
        return Result.success(recipeService.getRecipesByUserId(userId));
    }

    /**
     * 根据食材搜索菜谱（冰箱里有啥做啥）
     */
    @GetMapping("/by-ingredients")
    public Result<List<Recipe>> searchByIngredients(@RequestParam List<Long> ingredientIds) {
        return Result.success(recipeService.searchByIngredients(ingredientIds));
    }
}
