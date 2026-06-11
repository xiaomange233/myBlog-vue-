package com.ruoyi.system.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.system.domain.BlogArticle;
import com.ruoyi.system.domain.BlogTag;

/**
 * 博客文章 数据层
 */
public interface BlogArticleMapper
{
    public BlogArticle selectArticleById(Long articleId);

    public List<BlogArticle> selectArticleList(BlogArticle article);

    public int insertArticle(BlogArticle article);

    public int updateArticle(BlogArticle article);

    public int deleteArticleById(Long articleId);

    public int deleteArticleByIds(Long[] articleIds);

    public int updateArticleStatus(@Param("articleId") Long articleId, @Param("status") String status);

    public int insertArticleTag(@Param("articleId") Long articleId, @Param("tagId") Long tagId);

    public int deleteArticleTagsByArticleId(Long articleId);

    public int deleteArticleTagsByArticleIds(Long[] articleIds);

    public List<BlogTag> selectTagsByArticleId(Long articleId);
}
