package com.smartrecipe.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartrecipe.entity.Category;
import com.smartrecipe.exception.BusinessException;
import com.smartrecipe.mapper.CategoryMapper;
import com.smartrecipe.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 分类服务实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getAllCategories() {
        return categoryMapper.selectList(
                new LambdaQueryWrapper<Category>()
                        .orderByAsc(Category::getSortOrder)
        );
    }

    @Override
    public Category getCategoryById(Long id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new BusinessException(404, "分类不存在");
        }
        return category;
    }

    @Override
    public Long createCategory(Category category) {
        categoryMapper.insert(category);
        return category.getId();
    }

    @Override
    public void updateCategory(Category category) {
        if (categoryMapper.selectById(category.getId()) == null) {
            throw new BusinessException(404, "分类不存在");
        }
        categoryMapper.updateById(category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryMapper.deleteById(id);
    }
}
