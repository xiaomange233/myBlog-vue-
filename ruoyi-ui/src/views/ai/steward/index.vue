<template>
  <div class="app-container ai-steward">
    <div class="steward-shell">
      <aside class="steward-side">
        <div class="side-title">AI 管家</div>
        <div class="side-subtitle">平台管理员运营助手</div>

        <div class="prompt-list">
          <button
            v-for="item in quickPrompts"
            :key="item"
            class="prompt-item"
            type="button"
            @click="usePrompt(item)"
          >
            {{ item }}
          </button>
        </div>

        <el-button icon="el-icon-delete" size="mini" plain @click="clearChat">清空对话</el-button>
      </aside>

      <section class="steward-main">
        <div class="chat-panel" ref="chatPanel">
          <div v-if="messages.length === 0" class="empty-state">
            <div class="empty-title">今天想让管家帮你处理什么？</div>
            <div class="empty-text">可以让它帮你拟标题、写摘要、做 SEO 建议、规划文章选题或解释后台操作。</div>
          </div>

          <div
            v-for="(message, index) in messages"
            :key="index"
            class="chat-row"
            :class="message.role"
          >
            <div class="bubble">
              <div class="bubble-role">{{ message.role === 'user' ? '我' : 'AI 管家' }}</div>
              <div class="bubble-content">{{ message.content }}</div>
              <div v-if="message.elapsedMs" class="bubble-meta">{{ message.model }} · {{ message.elapsedMs }}ms</div>
            </div>
          </div>

          <div v-if="loading" class="chat-row assistant">
            <div class="bubble">
              <div class="bubble-role">AI 管家</div>
              <div class="bubble-content">正在思考...</div>
            </div>
          </div>
        </div>

        <div class="composer">
          <el-input
            v-model="prompt"
            type="textarea"
            :rows="4"
            resize="none"
            placeholder="输入你的问题，例如：帮我为一篇 Spring AI 博客生成 5 个标题"
            @keydown.native="handleKeydown"
          />
          <div class="composer-actions">
            <span class="hint">已保留最近 {{ maxContextMessages }} 条上下文，Enter 换行，Ctrl + Enter 发送</span>
            <el-button type="primary" icon="el-icon-position" :loading="loading" @click="sendMessage">发送</el-button>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import { chatWithSteward } from "@/api/ai/steward";

const STORAGE_KEY = "ai_steward_messages";
const MAX_CONTEXT_MESSAGES = 12;

export default {
  name: "AiSteward",
  data() {
    return {
      prompt: "",
      loading: false,
      maxContextMessages: MAX_CONTEXT_MESSAGES,
      messages: [],
      quickPrompts: [
        "帮我规划本周个人博客选题",
        "给这篇文章生成 SEO 标题和摘要",
        "帮我设计博客分类和标签体系",
        "检查这段文章开头是否吸引人",
        "总结后台博客管理的下一步优化"
      ]
    };
  },
  created() {
    this.loadMessages();
  },
  watch: {
    messages: {
      deep: true,
      handler() {
        this.saveMessages();
      }
    }
  },
  methods: {
    usePrompt(text) {
      this.prompt = text;
    },
    clearChat() {
      this.messages = [];
      this.removeSavedMessages();
    },
    handleKeydown(event) {
      if (event.ctrlKey && event.key === "Enter") {
        this.sendMessage();
      }
    },
    sendMessage() {
      const text = this.prompt && this.prompt.trim();
      if (!text) {
        this.$message.warning("请输入要咨询的问题");
        return;
      }
      if (this.loading) {
        return;
      }

      const context = this.buildContextMessages();
      const userMessage = this.createMessage("user", text);
      this.messages.push(userMessage);
      this.prompt = "";
      this.loading = true;
      this.scrollToBottom();

      chatWithSteward({ prompt: text, messages: context }).then(response => {
        const data = response.data;
        this.messages.push(this.createMessage("assistant", data.content, data.model, data.elapsedMs));
        this.scrollToBottom();
      }).catch(() => {
        this.messages = this.messages.filter(item => item.id !== userMessage.id);
        this.prompt = text;
      }).finally(() => {
        this.loading = false;
      });
    },
    createMessage(role, content, model, elapsedMs) {
      return {
        id: Date.now() + "-" + Math.random().toString(16).slice(2),
        role,
        content,
        model,
        elapsedMs,
        createTime: new Date().toISOString()
      };
    },
    buildContextMessages() {
      return this.messages
        .filter(item => item && (item.role === "user" || item.role === "assistant") && item.content)
        .slice(-MAX_CONTEXT_MESSAGES)
        .map(item => ({
          role: item.role,
          content: item.content
        }));
    },
    loadMessages() {
      try {
        const raw = localStorage.getItem(STORAGE_KEY);
        if (!raw) {
          return;
        }
        const list = JSON.parse(raw);
        if (Array.isArray(list)) {
          this.messages = list.filter(item => item && item.role && item.content);
          this.scrollToBottom();
        }
      } catch (e) {
        this.removeSavedMessages();
      }
    },
    saveMessages() {
      try {
        const list = this.messages.slice(-40);
        localStorage.setItem(STORAGE_KEY, JSON.stringify(list));
      } catch (e) {
      }
    },
    removeSavedMessages() {
      try {
        localStorage.removeItem(STORAGE_KEY);
      } catch (e) {
      }
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const panel = this.$refs.chatPanel;
        if (panel) {
          panel.scrollTop = panel.scrollHeight;
        }
      });
    }
  }
};
</script>

<style scoped>
.ai-steward {
  height: calc(100vh - 124px);
  min-height: 520px;
  overflow: hidden;
  background: #f5f7fb;
}

.steward-shell {
  display: grid;
  grid-template-columns: 260px minmax(0, 1fr);
  height: 100%;
  min-height: 0;
  overflow: hidden;
  border: 1px solid #e4e7ed;
  background: #fff;
}

.steward-side {
  display: flex;
  flex-direction: column;
  gap: 14px;
  padding: 18px;
  min-height: 0;
  overflow-y: auto;
  border-right: 1px solid #e4e7ed;
  background: #fbfcff;
}

.side-title {
  font-size: 20px;
  font-weight: 600;
  color: #1f2d3d;
}

.side-subtitle {
  margin-top: -8px;
  font-size: 13px;
  color: #8492a6;
}

.prompt-list {
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex: 1;
  margin-top: 6px;
}

.prompt-item {
  width: 100%;
  min-height: 38px;
  padding: 8px 10px;
  border: 1px solid #dcdfe6;
  background: #fff;
  color: #303133;
  text-align: left;
  cursor: pointer;
}

.prompt-item:hover {
  border-color: #409eff;
  color: #409eff;
}

.steward-main {
  display: flex;
  flex-direction: column;
  min-width: 0;
  min-height: 0;
  height: 100%;
  overflow: hidden;
}

.chat-panel {
  flex: 1;
  min-height: 0;
  overflow-y: auto;
  overflow-x: hidden;
  padding: 24px;
  scrollbar-gutter: stable;
}

.chat-panel::-webkit-scrollbar,
.steward-side::-webkit-scrollbar {
  width: 8px;
}

.chat-panel::-webkit-scrollbar-track,
.steward-side::-webkit-scrollbar-track {
  background: #f5f7fb;
}

.chat-panel::-webkit-scrollbar-thumb,
.steward-side::-webkit-scrollbar-thumb {
  background: #c0c4cc;
  border-radius: 4px;
}

.empty-state {
  max-width: 520px;
  margin: 12vh auto 0;
  text-align: center;
  color: #606266;
}

.empty-title {
  margin-bottom: 10px;
  font-size: 22px;
  font-weight: 600;
  color: #1f2d3d;
}

.empty-text {
  line-height: 1.7;
}

.chat-row {
  display: flex;
  margin-bottom: 16px;
}

.chat-row.user {
  justify-content: flex-end;
}

.chat-row.assistant {
  justify-content: flex-start;
}

.bubble {
  max-width: min(720px, 82%);
  padding: 12px 14px;
  border: 1px solid #e4e7ed;
  background: #fff;
  color: #303133;
  line-height: 1.7;
  white-space: pre-wrap;
}

.chat-row.user .bubble {
  border-color: #409eff;
  background: #ecf5ff;
}

.bubble-role {
  margin-bottom: 4px;
  font-size: 12px;
  font-weight: 600;
  color: #909399;
}

.bubble-meta {
  margin-top: 8px;
  font-size: 12px;
  color: #a8abb2;
}

.composer {
  flex-shrink: 0;
  padding: 16px;
  border-top: 1px solid #e4e7ed;
  background: #fff;
}

.composer-actions {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 10px;
}

.hint {
  color: #909399;
  font-size: 12px;
}

@media (max-width: 900px) {
  .ai-steward {
    height: calc(100vh - 124px);
    min-height: 560px;
  }

  .steward-shell {
    grid-template-columns: 1fr;
    grid-template-rows: auto minmax(0, 1fr);
  }

  .steward-side {
    max-height: 210px;
    border-right: 0;
    border-bottom: 1px solid #e4e7ed;
  }
}
</style>
