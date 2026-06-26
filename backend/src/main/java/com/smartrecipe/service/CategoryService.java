package com.smartrecipe.service;

import com.smartrecipe.entity.Category;
import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
    Long createCategory(Category category);
    void updateCategory(Category category);
    void deleteCategory(Long id);
}
