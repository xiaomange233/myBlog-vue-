package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.BlogTag;
import org.apache.ibatis.annotations.Mapper;

/**
 * 博客标签 数据层
 */
@Mapper
public interface BlogTagMapper
{
    public BlogTag selectTagById(Long tagId);

    public BlogTag selectTagByName(String tagName);

    public List<BlogTag> selectTagList(BlogTag tag);

    public int insertTag(BlogTag tag);

    public int updateTag(BlogTag tag);

    public int deleteTagById(Long tagId);

    public int deleteTagByIds(Long[] tagIds);
}
