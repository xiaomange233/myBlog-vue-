<template>
  <div class="app-container">
    <el-form ref="form" :model="form" :rules="rules" label-width="100px">
      <el-row>
        <el-col :span="24">
          <el-form-item label="文章标题" prop="title">
            <el-input v-model="form.title" placeholder="请输入文章标题" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文章分类" prop="categoryId">
            <el-select v-model="form.categoryId" placeholder="请选择分类" clearable>
              <el-option
                v-for="item in categoryOptions"
                :key="item.categoryId"
                :label="item.categoryName"
                :value="item.categoryId"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文章标签" prop="tagNames">
            <el-select
              v-model="form.tagNames"
              multiple
              filterable
              allow-create
              default-first-option
              placeholder="请选择或输入标签"
            >
              <el-option
                v-for="item in tagOptions"
                :key="item.tagId"
                :label="item.tagName"
                :value="item.tagName"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="文章封面" prop="coverImage">
            <image-upload v-model="form.coverImage"/>
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="文章摘要" prop="summary">
            <el-input
              v-model="form.summary"
              type="textarea"
              :rows="3"
              placeholder="请输入内容"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item label="文章内容" prop="content">
            <markdown-editor v-model="form.content" :min-height="300"/>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="置顶" prop="isTop">
            <el-radio-group v-model="form.isTop">
              <el-radio label="1">是</el-radio>
              <el-radio label="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="评论" prop="isComment">
            <el-radio-group v-model="form.isComment">
              <el-radio label="1">允许</el-radio>
              <el-radio label="0">禁止</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio label="0">草稿</el-radio>
              <el-radio label="1">发布</el-radio>
              <el-radio label="2">私密</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <div style="text-align: center; margin-top: 20px;">
      <el-button type="primary" @click="submitForm">保存</el-button>
      <el-button @click="cancel">取消</el-button>
    </div>
  </div>
</template>

<script>
import { getArticle, addArticle, updateArticle } from "@/api/blog/article";
import { listCategory } from "@/api/blog/category";
import { listTag } from "@/api/blog/tag";

export default {
  name: "ArticleEdit",
  data() {
    return {
      // 表单参数
      form: {
        articleId: undefined,
        title: undefined,
        content: '',
        summary: undefined,
        coverImage: undefined,
        status: '0',
        isTop: '0',
        isComment: '1',
        categoryName: undefined,
        categoryId: undefined,
        tagNames: []
      },
      // 表单校验
      rules: {
        title: [
          { required: true, message: "文章标题不能为空", trigger: "blur" }
        ],
        content: [
          { required: true, message: "文章内容不能为空", trigger: "blur" }
        ]
      },
      // 分类选项
      categoryOptions: [],
      // 标签选项
      tagOptions: []
    };
  },
  created() {
    this.getOptions();
    const articleId = this.$route.params && this.$route.params.articleId;
    if (articleId) {
      this.getDetails(articleId);
    }
  },
  methods: {
    // 获取分类和标签数据
    getOptions() {
      listCategory().then(response => {
        this.categoryOptions = response.rows;
      });
      listTag().then(response => {
        this.tagOptions = response.rows;
      });
    },
    // 获取文章详情
    getDetails(articleId) {
      getArticle(articleId).then(response => {
        this.form = response.data;
      });
    },
    // 取消按钮
    cancel() {
      this.$router.push("/blog/article");
    },
    // 提交按钮
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.articleId != null) {
            updateArticle(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.$router.push("/blog/article");
            });
          } else {
            addArticle(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.$router.push("/blog/article");
            });
          }
        }
      });
    }
  }
};
</script>
