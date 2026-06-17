<template>
  <div class="portal-page">
    <header class="portal-header">
      <div class="portal-inner header-inner">
        <div>
          <div class="brand">Personal Blog</div>
          <div class="tagline">记录技术、思考和日常构建</div>
        </div>
        <nav class="portal-nav">
          <button type="button" class="nav-link active" @click="goHome">文章</button>
          <button type="button" class="nav-link" @click="$router.push('/login')">后台</button>
        </nav>
      </div>
    </header>

    <main class="portal-inner portal-main">
      <section class="article-section">
        <div class="section-head">
          <div>
            <h1>最新文章</h1>
            <p>只展示已经发布的公开内容。</p>
          </div>
          <el-input
            v-model="queryParams.title"
            class="search-input"
            prefix-icon="el-icon-search"
            placeholder="搜索文章标题"
            clearable
            @keyup.enter.native="handleQuery"
            @clear="handleQuery"
          />
        </div>

        <div v-loading="loading" class="article-list">
          <article
            v-for="article in articleList"
            :key="article.articleId"
            class="article-item"
            @click="openArticle(article.articleId)"
          >
            <div v-if="article.coverImage" class="cover" :style="{ backgroundImage: 'url(' + article.coverImage + ')' }"></div>
            <div class="article-body">
              <div class="article-meta">
                <span>{{ article.categoryName || '未分类' }}</span>
                <span>{{ parseTime(article.createTime, '{y}-{m}-{d}') }}</span>
                <span>{{ article.viewCount || 0 }} 阅读</span>
              </div>
              <h2>{{ article.title }}</h2>
              <p>{{ article.summary || '这篇文章暂时没有摘要。' }}</p>
            </div>
          </article>
          <el-empty v-if="!loading && articleList.length === 0" description="暂无公开文章" />
        </div>

        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </section>

      <aside class="portal-side">
        <section class="side-block">
          <h3>分类</h3>
          <button
            v-for="category in categoryList"
            :key="category.categoryId"
            type="button"
            class="side-row"
            @click="selectCategory(category.categoryId)"
          >
            <span>{{ category.categoryName }}</span>
            <span>{{ category.articleCount || 0 }}</span>
          </button>
        </section>
        <section class="side-block">
          <h3>标签</h3>
          <div class="tag-cloud">
            <span v-for="tag in tagList" :key="tag.tagId" class="tag">{{ tag.tagName }}</span>
          </div>
        </section>
      </aside>
    </main>
  </div>
</template>

<script>
import { listPublicArticle, listPublicCategory, listPublicTag } from "@/api/open/blog";

export default {
  name: "PortalIndex",
  data() {
    return {
      loading: true,
      total: 0,
      articleList: [],
      categoryList: [],
      tagList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 8,
        title: undefined,
        categoryId: undefined
      }
    };
  },
  created() {
    this.getList();
    this.getSideData();
  },
  methods: {
    getList() {
      this.loading = true;
      listPublicArticle(this.queryParams).then(response => {
        this.articleList = response.rows;
        this.total = response.total;
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    getSideData() {
      listPublicCategory({ pageNum: 1, pageSize: 50 }).then(response => {
        this.categoryList = response.rows;
      });
      listPublicTag({ pageNum: 1, pageSize: 80 }).then(response => {
        this.tagList = response.rows;
      });
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    selectCategory(categoryId) {
      this.queryParams.categoryId = categoryId;
      this.handleQuery();
    },
    goHome() {
      this.queryParams.title = undefined;
      this.queryParams.categoryId = undefined;
      this.handleQuery();
    },
    openArticle(articleId) {
      const prefix = this.$route.path.indexOf("/front") === 0 ? "/front/article/" : "/portal/article/";
      this.$router.push(prefix + articleId);
    }
  }
};
</script>

<style scoped>
.portal-page {
  min-height: 100vh;
  background: #f6f8fb;
  color: #1f2d3d;
}

.portal-inner {
  width: min(1180px, calc(100vw - 40px));
  margin: 0 auto;
}

.portal-header {
  position: sticky;
  top: 0;
  z-index: 10;
  border-bottom: 1px solid #e4e7ed;
  background: rgba(255, 255, 255, 0.96);
}

.header-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-height: 72px;
}

.brand {
  font-size: 22px;
  font-weight: 700;
}

.tagline {
  margin-top: 4px;
  color: #6b7280;
  font-size: 13px;
}

.portal-nav {
  display: flex;
  gap: 10px;
}

.nav-link {
  border: 0;
  background: transparent;
  color: #606266;
  cursor: pointer;
  font-size: 14px;
}

.nav-link.active {
  color: #409eff;
  font-weight: 600;
}

.portal-main {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 280px;
  gap: 28px;
  padding: 32px 0;
}

.article-section,
.portal-side {
  min-width: 0;
}

.section-head {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 18px;
}

.section-head h1 {
  margin: 0;
  font-size: 30px;
}

.section-head p {
  margin: 8px 0 0;
  color: #6b7280;
}

.search-input {
  width: 280px;
}

.article-list {
  min-height: 320px;
}

.article-item {
  display: grid;
  grid-template-columns: 180px minmax(0, 1fr);
  gap: 18px;
  margin-bottom: 14px;
  padding: 16px;
  border: 1px solid #e4e7ed;
  background: #fff;
  cursor: pointer;
}

.article-item:hover {
  border-color: #409eff;
}

.cover {
  width: 100%;
  aspect-ratio: 16 / 10;
  background-size: cover;
  background-position: center;
  background-color: #e9edf3;
}

.article-body {
  min-width: 0;
}

.article-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  color: #909399;
  font-size: 13px;
}

.article-body h2 {
  margin: 10px 0 8px;
  font-size: 22px;
  line-height: 1.35;
}

.article-body p {
  margin: 0;
  color: #606266;
  line-height: 1.8;
}

.side-block {
  margin-bottom: 18px;
  padding: 16px;
  border: 1px solid #e4e7ed;
  background: #fff;
}

.side-block h3 {
  margin: 0 0 12px;
  font-size: 16px;
}

.side-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: 9px 0;
  border: 0;
  border-bottom: 1px solid #ebeef5;
  background: transparent;
  color: #303133;
  cursor: pointer;
}

.side-row:last-child {
  border-bottom: 0;
}

.tag-cloud {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag {
  padding: 5px 9px;
  background: #f1f5f9;
  color: #475569;
  font-size: 13px;
}

@media (max-width: 900px) {
  .portal-main {
    grid-template-columns: 1fr;
  }

  .section-head {
    align-items: stretch;
    flex-direction: column;
  }

  .search-input {
    width: 100%;
  }

  .article-item {
    grid-template-columns: 1fr;
  }
}
</style>
