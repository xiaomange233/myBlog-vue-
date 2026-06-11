package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BlogCategory;

/**
 * 博客分类 服务层
 */
public interface IBlogCategoryService
{
    public BlogCategory selectCategoryById(Long categoryId);

    public List<BlogCategory> selectCategoryList(BlogCategory category);

    public int insertCategory(BlogCategory category);

    public int updateCategory(BlogCategory category);

    public int deleteCategoryByIds(Long[] categoryIds);
}
