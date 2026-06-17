package com.ruoyi.web.controller.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.ai.AiChatRequest;
import com.ruoyi.system.service.IAiStewardService;

/**
 * 平台管理员 AI 管家
 */
@RestController
@RequestMapping("/ai/steward")
public class AiStewardController extends BaseController
{
    @Autowired
    private IAiStewardService aiStewardService;

    /**
     * 管家对话
     */
    @PreAuthorize("@ss.hasPermi('ai:steward:chat')")
    @Log(title = "AI管家", businessType = BusinessType.OTHER)
    @PostMapping("/chat")
    public AjaxResult chat(@RequestBody AiChatRequest request)
    {
        return success(aiStewardService.chat(request, getUsername()));
    }
}
