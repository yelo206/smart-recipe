package com.smartrecipe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartrecipe.entity.Ingredient;
import com.smartrecipe.exception.BusinessException;
import com.smartrecipe.mapper.IngredientMapper;
import com.smartrecipe.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 食材服务实现类
 */
@Service
public class IngredientServiceImpl implements IngredientService {

    @Autowired
    private IngredientMapper ingredientMapper;

    @Override
    public Page<Ingredient> getIngredientPage(int current, int size, String keyword) {
        Page<Ingredient> page = new Page<>(current, size);
        LambdaQueryWrapper<Ingredient> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Ingredient::getName, keyword);
        }
        wrapper.orderByDesc(Ingredient::getCreateTime);
        return ingredientMapper.selectPage(page, wrapper);
    }

    @Override
    public Ingredient getIngredientById(Long id) {
        Ingredient ingredient = ingredientMapper.selectById(id);
        if (ingredient == null) {
            throw new BusinessException(404, "食材不存在");
        }
        return ingredient;
    }

    @Override
    public Long createIngredient(Ingredient ingredient) {
        ingredientMapper.insert(ingredient);
        return ingredient.getId();
    }

    @Override
    public void updateIngredient(Ingredient ingredient) {
        if (ingredientMapper.selectById(ingredient.getId()) == null) {
            throw new BusinessException(404, "食材不存在");
        }
        ingredientMapper.updateById(ingredient);
    }

    @Override
    public void deleteIngredient(Long id) {
        ingredientMapper.deleteById(id);
    }

    @Override
    public List<Ingredient> getAll() {
        return ingredientMapper.selectList(null);
    }
}
