<template>
  <div class="portal-page">
    <header class="portal-header">
      <div class="portal-inner header-inner">
        <button type="button" class="back-link" @click="goBack">返回文章列表</button>
        <button type="button" class="back-link" @click="$router.push('/login')">后台</button>
      </div>
    </header>

    <main class="portal-inner detail-main" v-loading="loading">
      <article v-if="article" class="detail-article">
        <div class="article-meta">
          <span>{{ article.categoryName || '未分类' }}</span>
          <span>{{ parseTime(article.createTime, '{y}-{m}-{d}') }}</span>
          <span>{{ article.viewCount || 0 }} 阅读</span>
        </div>
        <h1>{{ article.title }}</h1>
        <p v-if="article.summary" class="summary">{{ article.summary }}</p>
        <div v-if="article.tagNames && article.tagNames.length" class="tags">
          <span v-for="tag in article.tagNames" :key="tag">{{ tag }}</span>
        </div>
        <div v-if="article.coverImage" class="hero-cover" :style="{ backgroundImage: 'url(' + article.coverImage + ')' }"></div>
        <mavon-editor
          class="markdown-view"
          :value="article.content || ''"
          :subfield="false"
          :defaultOpen="'preview'"
          :toolbarsFlag="false"
          :editable="false"
          :boxShadow="false"
          previewBackground="#ffffff"
        />
      </article>
      <el-empty v-if="!loading && !article" description="文章不存在或未发布" />
    </main>
  </div>
</template>

<script>
import { mavonEditor } from "mavon-editor";
import "mavon-editor/dist/css/index.css";
import { getPublicArticle } from "@/api/open/blog";

export default {
  name: "PortalDetail",
  components: { mavonEditor },
  data() {
    return {
      loading: true,
      article: null
    };
  },
  created() {
    this.getArticle();
  },
  methods: {
    goBack() {
      this.$router.push(this.$route.path.indexOf("/front") === 0 ? "/front" : "/portal");
    },
    getArticle() {
      this.loading = true;
      getPublicArticle(this.$route.params.articleId).then(response => {
        this.article = response.data;
        this.loading = false;
      }).catch(() => {
        this.article = null;
        this.loading = false;
      });
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
  width: min(920px, calc(100vw - 40px));
  margin: 0 auto;
}

.portal-header {
  border-bottom: 1px solid #e4e7ed;
  background: #fff;
}

.header-inner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-height: 64px;
}

.back-link {
  border: 0;
  background: transparent;
  color: #409eff;
  cursor: pointer;
  font-size: 14px;
}

.detail-main {
  min-height: 520px;
  padding: 34px 0 56px;
}

.detail-article {
  padding: 30px;
  border: 1px solid #e4e7ed;
  background: #fff;
}

.article-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  color: #909399;
  font-size: 13px;
}

h1 {
  margin: 14px 0 12px;
  font-size: 34px;
  line-height: 1.3;
}

.summary {
  margin: 0 0 16px;
  color: #606266;
  line-height: 1.8;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 18px;
}

.tags span {
  padding: 5px 9px;
  background: #f1f5f9;
  color: #475569;
  font-size: 13px;
}

.hero-cover {
  width: 100%;
  aspect-ratio: 16 / 7;
  margin-bottom: 22px;
  background-size: cover;
  background-position: center;
  background-color: #e9edf3;
}

.markdown-view {
  border: 0;
}

@media (max-width: 700px) {
  .detail-article {
    padding: 20px;
  }

  h1 {
    font-size: 28px;
  }
}
</style>
