package com.ruoyi.web.controller.open;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.BlogArticle;
import com.ruoyi.system.domain.BlogCategory;
import com.ruoyi.system.domain.BlogTag;
import com.ruoyi.system.service.IBlogArticleService;
import com.ruoyi.system.service.IBlogCategoryService;
import com.ruoyi.system.service.IBlogTagService;

/**
 * 公开博客文章展示接口
 */
@Anonymous
@RestController
@RequestMapping("/open/blog")
public class OpenBlogArticleController extends BaseController
{
    @Autowired
    private IBlogArticleService articleService;

    @Autowired
    private IBlogCategoryService categoryService;

    @Autowired
    private IBlogTagService tagService;

    /**
     * 查询已发布文章列表
     */
    @GetMapping("/article/list")
    public TableDataInfo list(BlogArticle article)
    {
        startPage();
        article.setStatus("1");
        List<BlogArticle> list = articleService.selectArticleList(article);
        return getDataTable(list);
    }

    /**
     * 查询已发布文章详情
     */
    @GetMapping("/article/{articleId}")
    public AjaxResult getInfo(@PathVariable Long articleId)
    {
        BlogArticle article = articleService.selectPublishedArticleById(articleId);
        return article == null ? error("文章不存在或未发布") : success(article);
    }

    /**
     * 查询启用分类
     */
    @GetMapping("/category/list")
    public TableDataInfo categoryList(BlogCategory category)
    {
        startPage();
        category.setStatus("0");
        List<BlogCategory> list = categoryService.selectCategoryList(category);
        return getDataTable(list);
    }

    /**
     * 查询启用标签
     */
    @GetMapping("/tag/list")
    public TableDataInfo tagList(BlogTag tag)
    {
        startPage();
        tag.setStatus("0");
        List<BlogTag> list = tagService.selectTagList(tag);
        return getDataTable(list);
    }
}
