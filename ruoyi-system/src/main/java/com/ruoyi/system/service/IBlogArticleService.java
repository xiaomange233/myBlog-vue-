package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.BlogArticle;

/**
 * 博客文章 服务层
 */
public interface IBlogArticleService
{
    public BlogArticle selectArticleById(Long articleId);

    public List<BlogArticle> selectArticleList(BlogArticle article);

    public BlogArticle selectPublishedArticleById(Long articleId);

    public int insertArticle(BlogArticle article);

    public int updateArticle(BlogArticle article);

    public int deleteArticleByIds(Long[] articleIds);

    public int updateArticleStatus(Long articleId, String status);
}
