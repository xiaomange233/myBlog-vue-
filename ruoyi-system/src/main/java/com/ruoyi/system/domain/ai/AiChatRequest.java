package com.ruoyi.system.domain.ai;

import java.util.List;

/**
 * AI 管家对话请求
 */
public class AiChatRequest
{
    /** 当前提问 */
    private String prompt;

    /** 最近对话上下文 */
    private List<AiChatMessage> messages;

    public String getPrompt()
    {
        return prompt;
    }

    public void setPrompt(String prompt)
    {
        this.prompt = prompt;
    }

    public List<AiChatMessage> getMessages()
    {
        return messages;
    }

    public void setMessages(List<AiChatMessage> messages)
    {
        this.messages = messages;
    }
}
