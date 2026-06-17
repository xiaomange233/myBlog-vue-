package com.ruoyi.system.domain.ai;

/**
 * AI 对话消息
 */
public class AiChatMessage
{
    /** 角色：user / assistant / system */
    private String role;

    /** 消息内容 */
    private String content;

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
