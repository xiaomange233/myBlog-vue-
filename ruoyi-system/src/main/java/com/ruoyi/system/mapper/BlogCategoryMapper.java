package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BlogCategory;

/**
 * 博客分类 数据层
 */
public interface BlogCategoryMapper
{
    public BlogCategory selectCategoryById(Long categoryId);

    public List<BlogCategory> selectCategoryList(BlogCategory category);

    public int insertCategory(BlogCategory category);

    public int updateCategory(BlogCategory category);

    public int deleteCategoryById(Long categoryId);

    public int deleteCategoryByIds(Long[] categoryIds);
}
