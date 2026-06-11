package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.BlogCategory;
import com.ruoyi.system.mapper.BlogCategoryMapper;
import com.ruoyi.system.service.IBlogCategoryService;

/**
 * 博客分类 服务层实现
 */
@Service
public class BlogCategoryServiceImpl implements IBlogCategoryService
{
    @Autowired
    private BlogCategoryMapper categoryMapper;

    @Override
    public BlogCategory selectCategoryById(Long categoryId)
    {
        return categoryMapper.selectCategoryById(categoryId);
    }

    @Override
    public List<BlogCategory> selectCategoryList(BlogCategory category)
    {
        return categoryMapper.selectCategoryList(category);
    }

    @Override
    public int insertCategory(BlogCategory category)
    {
        return categoryMapper.insertCategory(category);
    }

    @Override
    public int updateCategory(BlogCategory category)
    {
        return categoryMapper.updateCategory(category);
    }

    @Override
    public int deleteCategoryByIds(Long[] categoryIds)
    {
        return categoryMapper.deleteCategoryByIds(categoryIds);
    }
}
