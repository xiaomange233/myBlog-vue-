package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.BlogArticle;
import com.ruoyi.system.domain.BlogTag;
import com.ruoyi.system.mapper.BlogArticleMapper;
import com.ruoyi.system.mapper.BlogTagMapper;
import com.ruoyi.system.service.IBlogArticleService;

/**
 * 博客文章 服务层实现
 */
@Service
public class BlogArticleServiceImpl implements IBlogArticleService
{
    @Autowired
    private BlogArticleMapper articleMapper;

    @Autowired
    private BlogTagMapper tagMapper;

    @Override
    public BlogArticle selectArticleById(Long articleId)
    {
        BlogArticle article = articleMapper.selectArticleById(articleId);
        if (article != null)
        {
            List<BlogTag> tags = articleMapper.selectTagsByArticleId(articleId);
            article.setTagNames(tags.stream().map(BlogTag::getTagName).toArray(String[]::new));
        }
        return article;
    }

    @Override
    public List<BlogArticle> selectArticleList(BlogArticle article)
    {
        return articleMapper.selectArticleList(article);
    }

    @Override
    public BlogArticle selectPublishedArticleById(Long articleId)
    {
        BlogArticle article = articleMapper.selectPublishedArticleById(articleId);
        if (article != null)
        {
            List<BlogTag> tags = articleMapper.selectTagsByArticleId(articleId);
            article.setTagNames(tags.stream().map(BlogTag::getTagName).toArray(String[]::new));
            articleMapper.updateViewCount(articleId, 1);
            article.setViewCount(article.getViewCount() == null ? 1 : article.getViewCount() + 1);
        }
        return article;
    }

    @Override
    @Transactional
    public int insertArticle(BlogArticle article)
    {
        int rows = articleMapper.insertArticle(article);
        insertArticleTags(article);
        return rows;
    }

    @Override
    @Transactional
    public int updateArticle(BlogArticle article)
    {
        articleMapper.deleteArticleTagsByArticleId(article.getArticleId());
        insertArticleTags(article);
        return articleMapper.updateArticle(article);
    }

    @Override
    @Transactional
    public int deleteArticleByIds(Long[] articleIds)
    {
        articleMapper.deleteArticleTagsByArticleIds(articleIds);
        return articleMapper.deleteArticleByIds(articleIds);
    }

    @Override
    public int updateArticleStatus(Long articleId, String status)
    {
        return articleMapper.updateArticleStatus(articleId, status);
    }

    private void insertArticleTags(BlogArticle article)
    {
        if (article.getArticleId() == null || StringUtils.isEmpty(article.getTagNames()))
        {
            return;
        }
        for (String tagName : article.getTagNames())
        {
            if (StringUtils.isBlank(tagName))
            {
                continue;
            }
            BlogTag tag = tagMapper.selectTagByName(tagName.trim());
            if (tag == null)
            {
                tag = new BlogTag();
                tag.setTagName(tagName.trim());
                tag.setStatus("0");
                tagMapper.insertTag(tag);
            }
            articleMapper.insertArticleTag(article.getArticleId(), tag.getTagId());
        }
    }
}
