package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.BlogTag;
import com.ruoyi.system.mapper.BlogTagMapper;
import com.ruoyi.system.service.IBlogTagService;

/**
 * 博客标签 服务层实现
 */
@Service
public class BlogTagServiceImpl implements IBlogTagService
{
    @Autowired
    private BlogTagMapper tagMapper;

    @Override
    public BlogTag selectTagById(Long tagId)
    {
        return tagMapper.selectTagById(tagId);
    }

    @Override
    public BlogTag selectTagByName(String tagName)
    {
        return tagMapper.selectTagByName(tagName);
    }

    @Override
    public List<BlogTag> selectTagList(BlogTag tag)
    {
        return tagMapper.selectTagList(tag);
    }

    @Override
    public int insertTag(BlogTag tag)
    {
        return tagMapper.insertTag(tag);
    }

    @Override
    public int updateTag(BlogTag tag)
    {
        return tagMapper.updateTag(tag);
    }

    @Override
    public int deleteTagByIds(Long[] tagIds)
    {
        return tagMapper.deleteTagByIds(tagIds);
    }
}
