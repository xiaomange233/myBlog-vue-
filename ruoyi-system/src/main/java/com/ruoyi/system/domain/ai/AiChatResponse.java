package com.ruoyi.system.domain.ai;

/**
 * AI 管家对话响应
 */
public class AiChatResponse
{
    /** AI 回复内容 */
    private String content;

    /** 使用的模型 */
    private String model;

    /** 调用耗时 */
    private Long elapsedMs;

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }

    public Long getElapsedMs()
    {
        return elapsedMs;
    }

    public void setElapsedMs(Long elapsedMs)
    {
        this.elapsedMs = elapsedMs;
    }
}
