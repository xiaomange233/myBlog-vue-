package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BlogTag;

/**
 * 博客标签 服务层
 */
public interface IBlogTagService
{
    public BlogTag selectTagById(Long tagId);

    public BlogTag selectTagByName(String tagName);

    public List<BlogTag> selectTagList(BlogTag tag);

    public int insertTag(BlogTag tag);

    public int updateTag(BlogTag tag);

    public int deleteTagByIds(Long[] tagIds);
}
