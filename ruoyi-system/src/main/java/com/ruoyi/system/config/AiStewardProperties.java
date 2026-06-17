package com.ruoyi.system.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 平台管理员 AI 管家配置
 */
@Component
@ConfigurationProperties(prefix = "ruoyi.ai")
public class AiStewardProperties
{
    /** 是否启用 */
    private boolean enabled = false;

    /** OpenAI-compatible chat completions 地址 */
    private String baseUrl = "https://api.openai.com/v1/chat/completions";

    /** API Key */
    private String apiKey;

    /** 模型名称 */
    private String model = "gpt-4o-mini";

    /** 温度 */
    private Double temperature = 0.3D;

    /** Spring AI 风格聊天配置 */
    private Chat chat = new Chat();

    /** 最大输出 token */
    private Integer maxTokens = 1200;

    /** 请求超时时间 */
    private Integer timeout = 30000;

    /** 系统提示词 */
    private String systemPrompt = "你是个人博客后台的平台管理员 AI 管家。回答要简洁、可执行，优先帮助管理员完成内容运营、文章选题、摘要润色、SEO 建议、后台功能使用和故障排查。";

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public String getBaseUrl()
    {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
    }

    public String getModel()
    {
        if (chat != null && chat.getOptions() != null && chat.getOptions().getModel() != null)
        {
            return chat.getOptions().getModel();
        }
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public Double getTemperature()
    {
        if (chat != null && chat.getOptions() != null && chat.getOptions().getTemperature() != null)
        {
            return chat.getOptions().getTemperature();
        }
        return temperature;
    }

    public void setTemperature(Double temperature)
    {
        this.temperature = temperature;
    }

    public Chat getChat()
    {
        return chat;
    }

    public void setChat(Chat chat)
    {
        this.chat = chat;
    }

    public Integer getMaxTokens()
    {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens)
    {
        this.maxTokens = maxTokens;
    }

    public Integer getTimeout()
    {
        return timeout;
    }

    public void setTimeout(Integer timeout)
    {
        this.timeout = timeout;
    }

    public String getSystemPrompt()
    {
        return systemPrompt;
    }

    public void setSystemPrompt(String systemPrompt)
    {
        this.systemPrompt = systemPrompt;
    }

    public static class Chat
    {
        private Options options = new Options();

        public Options getOptions()
        {
            return options;
        }

        public void setOptions(Options options)
        {
            this.options = options;
        }
    }

    public static class Options
    {
        /** 模型名称 */
        private String model;

        /** 温度 */
        private Double temperature;

        public String getModel()
        {
            return model;
        }

        public void setModel(String model)
        {
            this.model = model;
        }

        public Double getTemperature()
        {
            return temperature;
        }

        public void setTemperature(Double temperature)
        {
            this.temperature = temperature;
        }
    }
}
