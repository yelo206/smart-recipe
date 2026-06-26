package com.smartrecipe.controller;

import com.smartrecipe.entity.Category;
import com.smartrecipe.service.CategoryService;
import com.smartrecipe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 分类控制器
 */
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result<List<Category>> list() {
        return Result.success(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable Long id) {
        return Result.success(categoryService.getCategoryById(id));
    }

    @PostMapping
    public Result<Long> create(@RequestBody Category category) {
        return Result.success("创建成功", categoryService.createCategory(category));
    }

    @PutMapping
    public Result<Void> update(@RequestBody Category category) {
        categoryService.updateCategory(category);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success("删除成功", null);
    }
}
