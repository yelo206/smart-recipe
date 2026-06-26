package com.smartrecipe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartrecipe.dto.RecipeDTO;
import com.smartrecipe.entity.*;
import com.smartrecipe.exception.BusinessException;
import com.smartrecipe.mapper.*;
import com.smartrecipe.service.RecipeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜谱服务实现类
 */
@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeMapper recipeMapper;
    @Autowired
    private RecipeIngredientMapper recipeIngredientMapper;
    @Autowired
    private RecipeStepMapper recipeStepMapper;
    @Autowired
    private IngredientMapper ingredientMapper;

    @Override
    public Page<Recipe> getRecipePage(int current, int size, String keyword, Long categoryId) {
        Page<Recipe> page = new Page<>(current, size);
        LambdaQueryWrapper<Recipe> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Recipe::getTitle, keyword);
        }
        if (categoryId != null) {
            wrapper.eq(Recipe::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(Recipe::getCreateTime);
        return recipeMapper.selectPage(page, wrapper);
    }

    @Override
    public Recipe getRecipeById(Long id) {
        Recipe recipe = recipeMapper.selectById(id);
        if (recipe == null) {
            throw new BusinessException(404, "菜谱不存在");
        }
        return recipe;
    }

    @Override
    public RecipeDTO getRecipeDetail(Long id) {
        Recipe recipe = getRecipeById(id);
        RecipeDTO dto = new RecipeDTO();
        BeanUtils.copyProperties(recipe, dto);

        // 查询食材列表
        List<RecipeIngredient> recipeIngredients = recipeIngredientMapper.selectList(
                new LambdaQueryWrapper<RecipeIngredient>()
                        .eq(RecipeIngredient::getRecipeId, id)
        );
        // 批量查询食材名称
        List<Long> ingredientIds = recipeIngredients.stream()
                .map(RecipeIngredient::getIngredientId).toList();
        Map<Long, Ingredient> ingredientMap = ingredientIds.isEmpty() 
                ? Map.of() 
                : ingredientMapper.selectBatchIds(ingredientIds).stream()
                .collect(Collectors.toMap(Ingredient::getId, i -> i));
        dto.setIngredients(recipeIngredients.stream().map(ri -> {
            RecipeDTO.IngredientItem item = new RecipeDTO.IngredientItem();
            item.setIngredientId(ri.getIngredientId());
            Ingredient ing = ingredientMap.get(ri.getIngredientId());
            item.setName(ing != null ? ing.getName() : "未知食材");
            item.setAmount(ri.getAmount());
            item.setUnit(ri.getUnit());
            return item;
        }).toList());

        // 查询步骤列表
        List<RecipeStep> steps = recipeStepMapper.selectList(
                new LambdaQueryWrapper<RecipeStep>()
                        .eq(RecipeStep::getRecipeId, id)
                        .orderByAsc(RecipeStep::getStepNumber)
        );
        dto.setSteps(steps.stream().map(step -> {
            RecipeDTO.StepItem item = new RecipeDTO.StepItem();
            item.setStepNumber(step.getStepNumber());
            item.setDescription(step.getDescription());
            item.setImageUrl(step.getImageUrl());
            return item;
        }).toList());

        return dto;
    }

    @Override
    @Transactional
    public Long createRecipe(RecipeDTO recipeDTO, Long userId) {
        // 创建菜谱
        Recipe recipe = new Recipe();
        BeanUtils.copyProperties(recipeDTO, recipe);
        recipe.setUserId(userId);
        recipeMapper.insert(recipe);

        Long recipeId = recipe.getId();

        // 保存食材关联
        if (recipeDTO.getIngredients() != null) {
            for (RecipeDTO.IngredientItem item : recipeDTO.getIngredients()) {
                RecipeIngredient ri = new RecipeIngredient();
                ri.setRecipeId(recipeId);
                ri.setIngredientId(item.getIngredientId());
                ri.setAmount(item.getAmount());
                ri.setUnit(item.getUnit());
                recipeIngredientMapper.insert(ri);
            }
        }

        // 保存步骤
        if (recipeDTO.getSteps() != null) {
            for (RecipeDTO.StepItem step : recipeDTO.getSteps()) {
                RecipeStep recipeStep = new RecipeStep();
                recipeStep.setRecipeId(recipeId);
                recipeStep.setStepNumber(step.getStepNumber());
                recipeStep.setDescription(step.getDescription());
                recipeStep.setImageUrl(step.getImageUrl());
                recipeStepMapper.insert(recipeStep);
            }
        }

        return recipeId;
    }

    @Override
    @Transactional
    public void updateRecipe(RecipeDTO recipeDTO) {
        if (recipeDTO.getId() == null) {
            throw new BusinessException(400, "菜谱ID不能为空");
        }
        Recipe recipe = new Recipe();
        BeanUtils.copyProperties(recipeDTO, recipe);
        recipeMapper.updateById(recipe);

        Long recipeId = recipeDTO.getId();

        // 删除旧食材关联
        recipeIngredientMapper.delete(
                new LambdaQueryWrapper<RecipeIngredient>()
                        .eq(RecipeIngredient::getRecipeId, recipeId)
        );
        // 保存新食材关联
        if (recipeDTO.getIngredients() != null) {
            for (RecipeDTO.IngredientItem item : recipeDTO.getIngredients()) {
                RecipeIngredient ri = new RecipeIngredient();
                ri.setRecipeId(recipeId);
                ri.setIngredientId(item.getIngredientId());
                ri.setAmount(item.getAmount());
                ri.setUnit(item.getUnit());
                recipeIngredientMapper.insert(ri);
            }
        }

        // 删除旧步骤
        recipeStepMapper.delete(
                new LambdaQueryWrapper<RecipeStep>()
                        .eq(RecipeStep::getRecipeId, recipeId)
        );
        // 保存新步骤
        if (recipeDTO.getSteps() != null) {
            for (RecipeDTO.StepItem step : recipeDTO.getSteps()) {
                RecipeStep recipeStep = new RecipeStep();
                recipeStep.setRecipeId(recipeId);
                recipeStep.setStepNumber(step.getStepNumber());
                recipeStep.setDescription(step.getDescription());
                recipeStep.setImageUrl(step.getImageUrl());
                recipeStepMapper.insert(recipeStep);
            }
        }
    }

    @Override
    @Transactional
    public void deleteRecipe(Long id) {
        recipeMapper.deleteById(id);
        recipeIngredientMapper.delete(
                new LambdaQueryWrapper<RecipeIngredient>()
                        .eq(RecipeIngredient::getRecipeId, id)
        );
        recipeStepMapper.delete(
                new LambdaQueryWrapper<RecipeStep>()
                        .eq(RecipeStep::getRecipeId, id)
        );
    }

    @Override
    public List<Recipe> getRecipesByUserId(Long userId) {
        return recipeMapper.selectList(
                new LambdaQueryWrapper<Recipe>()
                        .eq(Recipe::getUserId, userId)
                        .orderByDesc(Recipe::getCreateTime)
        );
    }

    @Override
    public List<Recipe> searchByIngredients(List<Long> ingredientIds) {
        if (ingredientIds == null || ingredientIds.isEmpty()) {
            return List.of();
        }
        // 查询使用了这些食材的菜谱-食材关联记录
        List<RecipeIngredient> matches = recipeIngredientMapper.selectList(
                new LambdaQueryWrapper<RecipeIngredient>()
                        .in(RecipeIngredient::getIngredientId, ingredientIds)
        );
        if (matches.isEmpty()) {
            return List.of();
        }
        // 按菜谱ID分组，统计每个菜谱匹配的食材数量
        Map<Long, Long> matchCountMap = matches.stream()
                .collect(Collectors.groupingBy(RecipeIngredient::getRecipeId, Collectors.counting()));
        // 查询匹配的菜谱详情
        List<Recipe> recipes = recipeMapper.selectBatchIds(matchCountMap.keySet());
        // 设置匹配数量并按降序排序
        recipes.forEach(r -> r.setMatchCount(matchCountMap.get(r.getId()).intValue()));
        recipes.sort((a, b) -> b.getMatchCount() - a.getMatchCount());
        return recipes;
    }
}
