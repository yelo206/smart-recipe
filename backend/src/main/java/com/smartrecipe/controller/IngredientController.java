package com.smartrecipe.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartrecipe.entity.Ingredient;
import com.smartrecipe.service.IngredientService;
import com.smartrecipe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 食材控制器
 */
@RestController
@RequestMapping("/ingredient")
public class IngredientController {

    @Autowired
    private IngredientService ingredientService;

    @GetMapping("/page")
    public Result<Page<Ingredient>> getPage(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        return Result.success(ingredientService.getIngredientPage(current, size, keyword));
    }

    /** 获取全部食材列表（不分页） */
    @GetMapping("/list")
    public Result<List<Ingredient>> getList() {
        return Result.success(ingredientService.getAll());
    }

    @GetMapping("/{id}")
    public Result<Ingredient> getById(@PathVariable Long id) {
        return Result.success(ingredientService.getIngredientById(id));
    }

    @PostMapping
    public Result<Long> create(@RequestBody Ingredient ingredient) {
        return Result.success("创建成功", ingredientService.createIngredient(ingredient));
    }

    @PutMapping
    public Result<Void> update(@RequestBody Ingredient ingredient) {
        ingredientService.updateIngredient(ingredient);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return Result.success("删除成功", null);
    }
}
