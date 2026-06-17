package com.ruoyi.system.service;

import com.ruoyi.system.domain.ai.AiChatRequest;
import com.ruoyi.system.domain.ai.AiChatResponse;

/**
 * 平台管理员 AI 管家服务
 */
public interface IAiStewardService
{
    public AiChatResponse chat(AiChatRequest request, String username);
}
