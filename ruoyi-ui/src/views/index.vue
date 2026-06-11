<template>
  <div class="app-container blog-home">
    <section class="hero-panel">
      <div>
        <p class="eyebrow">Personal Blog Console</p>
        <h1>小满哥的个人博客</h1>
        <p class="intro">
          用来记录技术笔记、生活片段和长期思考。后台重点保留文章、分类、标签这些写作相关能力，让维护博客这件事更轻一点。
        </p>
      </div>
      <div class="quick-actions">
        <el-button type="primary" icon="el-icon-edit" @click="$router.push('/blog/article/edit')">写文章</el-button>
        <el-button icon="el-icon-notebook-2" @click="$router.push('/blog/article')">文章管理</el-button>
      </div>
    </section>

    <el-row :gutter="16" class="summary-row">
      <el-col :xs="24" :sm="8">
        <div class="summary-card">
          <svg-icon icon-class="edit" />
          <div>
            <strong>文章</strong>
            <span>草稿、发布和私密内容统一维护</span>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="8">
        <div class="summary-card">
          <svg-icon icon-class="tree" />
          <div>
            <strong>分类</strong>
            <span>按主题整理内容结构</span>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="8">
        <div class="summary-card">
          <svg-icon icon-class="dict" />
          <div>
            <strong>标签</strong>
            <span>给文章补充细粒度索引</span>
          </div>
        </div>
      </el-col>
    </el-row>

    <section class="writing-board">
      <div class="board-title">
        <h2>写作工作台</h2>
        <el-button type="text" icon="el-icon-arrow-right" @click="$router.push('/blog/article')">进入文章列表</el-button>
      </div>
      <el-row :gutter="16">
        <el-col :xs="24" :md="8">
          <div class="task-card">
            <span class="step">01</span>
            <h3>沉淀想法</h3>
            <p>先写草稿，把灵感、代码片段、阅读摘记都收进来。</p>
          </div>
        </el-col>
        <el-col :xs="24" :md="8">
          <div class="task-card">
            <span class="step">02</span>
            <h3>整理结构</h3>
            <p>用分类确定主题归属，用标签串起相关内容。</p>
          </div>
        </el-col>
        <el-col :xs="24" :md="8">
          <div class="task-card">
            <span class="step">03</span>
            <h3>发布文章</h3>
            <p>补好摘要和封面，确认状态后发布到博客前台。</p>
          </div>
        </el-col>
      </el-row>
    </section>
  </div>
</template>

<script>
export default {
  name: "Index"
};
</script>

<style scoped lang="scss">
.blog-home {
  background: #f6f8fb;
  min-height: calc(100vh - 84px);
}

.hero-panel {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  padding: 32px;
  border-radius: 8px;
  background: linear-gradient(135deg, #1f2937 0%, #355c7d 56%, #5b8c85 100%);
  color: #fff;
}

.eyebrow {
  margin: 0 0 8px;
  font-size: 12px;
  text-transform: uppercase;
  color: rgba(255, 255, 255, 0.72);
}

h1 {
  margin: 0;
  font-size: 30px;
  font-weight: 700;
}

.intro {
  max-width: 640px;
  margin: 14px 0 0;
  line-height: 1.8;
  color: rgba(255, 255, 255, 0.86);
}

.quick-actions {
  display: flex;
  flex-shrink: 0;
  gap: 12px;
}

.summary-row {
  margin-top: 16px;
}

.summary-card,
.task-card {
  border-radius: 8px;
  background: #fff;
  box-shadow: 0 8px 20px rgba(31, 41, 55, 0.06);
}

.summary-card {
  display: flex;
  align-items: center;
  gap: 16px;
  min-height: 108px;
  padding: 22px;
}

.summary-card .svg-icon {
  width: 30px;
  height: 30px;
  color: #355c7d;
}

.summary-card strong {
  display: block;
  margin-bottom: 8px;
  font-size: 17px;
  color: #1f2937;
}

.summary-card span,
.task-card p {
  color: #667085;
  line-height: 1.7;
}

.writing-board {
  margin-top: 16px;
  padding: 24px;
  border-radius: 8px;
  background: #fff;
}

.board-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.board-title h2 {
  margin: 0;
  color: #1f2937;
  font-size: 20px;
}

.task-card {
  min-height: 160px;
  padding: 22px;
  border: 1px solid #edf0f5;
  box-shadow: none;
}

.step {
  color: #5b8c85;
  font-size: 13px;
  font-weight: 700;
}

.task-card h3 {
  margin: 10px 0 8px;
  color: #1f2937;
}

@media (max-width: 768px) {
  .hero-panel {
    display: block;
    padding: 24px;
  }

  .quick-actions {
    margin-top: 20px;
  }

  .summary-card,
  .task-card {
    margin-bottom: 12px;
  }
}
</style>
