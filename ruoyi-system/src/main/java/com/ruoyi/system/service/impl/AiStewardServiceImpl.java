package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.config.AiStewardProperties;
import com.ruoyi.system.domain.ai.AiChatMessage;
import com.ruoyi.system.domain.ai.AiChatRequest;
import com.ruoyi.system.domain.ai.AiChatResponse;
import com.ruoyi.system.service.IAiStewardService;

/**
 * 平台管理员 AI 管家服务实现
 */
@Service
public class AiStewardServiceImpl implements IAiStewardService
{
    private static final int MAX_CONTEXT_MESSAGES = 12;

    @Autowired
    private AiStewardProperties properties;

    @Override
    public AiChatResponse chat(AiChatRequest request, String username)
    {
        validateRequest(request);

        long start = System.nanoTime();
        JSONObject payload = buildPayload(request, username);
        HttpHeaders headers = buildHeaders();

        try
        {
            ResponseEntity<String> response = createRestTemplate().postForEntity(
                    resolveChatCompletionsUrl(),
                    new HttpEntity<>(payload.toJSONString(), headers),
                    String.class
            );
            JSONObject body = JSONObject.parseObject(response.getBody());
            String content = parseContent(body);

            AiChatResponse chatResponse = new AiChatResponse();
            chatResponse.setContent(content);
            chatResponse.setModel(body.getString("model") != null ? body.getString("model") : properties.getModel());
            chatResponse.setElapsedMs(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start));
            return chatResponse;
        }
        catch (ServiceException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            throw new ServiceException("AI 管家调用失败：" + e.getMessage());
        }
    }

    private void validateRequest(AiChatRequest request)
    {
        if (!properties.isEnabled())
        {
            throw new ServiceException("AI 管家未启用，请先配置 ruoyi.ai.enabled=true");
        }
        if (StringUtils.isBlank(properties.getApiKey()))
        {
            throw new ServiceException("AI 管家 API Key 未配置");
        }
        if (request == null || StringUtils.isBlank(request.getPrompt()))
        {
            throw new ServiceException("请输入要咨询的问题");
        }
    }

    private JSONObject buildPayload(AiChatRequest request, String username)
    {
        JSONArray messages = new JSONArray();
        messages.add(message("system", buildSystemPrompt(username)));

        appendContextMessages(messages, request.getMessages());

        messages.add(message("user", request.getPrompt()));

        JSONObject payload = new JSONObject();
        payload.put("model", properties.getModel());
        payload.put("messages", messages);
        payload.put("temperature", properties.getTemperature());
        payload.put("max_tokens", properties.getMaxTokens());
        return payload;
    }

    private String buildSystemPrompt(String username)
    {
        String operator = StringUtils.isNotBlank(username) ? username : "admin";
        return properties.getSystemPrompt() + "\n当前登录管理员：" + operator
                + "\n你可以协助：文章选题、标题优化、摘要生成、SEO 关键词、分类标签建议、内容审核建议、后台操作说明。"
                + "\n不要编造后台数据；如果需要真实数据，请提示管理员在对应页面查询。";
    }

    private HttpHeaders buildHeaders()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(properties.getApiKey());
        return headers;
    }

    private RestTemplate createRestTemplate()
    {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getTimeout());
        factory.setReadTimeout(properties.getTimeout());
        return new RestTemplate(factory);
    }

    private String resolveChatCompletionsUrl()
    {
        String baseUrl = properties.getBaseUrl();
        if (StringUtils.isBlank(baseUrl))
        {
            throw new ServiceException("AI 管家 base-url 未配置");
        }
        baseUrl = baseUrl.trim();
        while (baseUrl.endsWith("/"))
        {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        if (baseUrl.endsWith("/chat/completions"))
        {
            return baseUrl;
        }
        return baseUrl + "/chat/completions";
    }

    private boolean isSupportedRole(String role)
    {
        return "user".equals(role) || "assistant".equals(role);
    }

    private void appendContextMessages(JSONArray messages, List<AiChatMessage> context)
    {
        if (context == null || context.isEmpty())
        {
            return;
        }

        JSONArray cleaned = new JSONArray();
        for (AiChatMessage item : context)
        {
            if (item == null || StringUtils.isBlank(item.getContent()))
            {
                continue;
            }
            String role = normalizeRole(item.getRole());
            if (!isSupportedRole(role))
            {
                continue;
            }
            cleaned.add(message(role, item.getContent()));
        }

        int startIndex = Math.max(0, cleaned.size() - MAX_CONTEXT_MESSAGES);
        for (int i = startIndex; i < cleaned.size(); i++)
        {
            messages.add(cleaned.getJSONObject(i));
        }
    }

    private String normalizeRole(String role)
    {
        return role == null ? "" : role.trim().toLowerCase();
    }

    private JSONObject message(String role, String content)
    {
        JSONObject message = new JSONObject();
        message.put("role", role);
        message.put("content", content);
        return message;
    }

    private String parseContent(JSONObject body)
    {
        if (body == null)
        {
            throw new ServiceException("AI 管家返回为空");
        }
        JSONArray choices = body.getJSONArray("choices");
        if (choices == null || choices.isEmpty())
        {
            throw new ServiceException("AI 管家没有返回可用回答");
        }
        JSONObject choice = choices.getJSONObject(0);
        JSONObject message = choice.getJSONObject("message");
        if (message == null || StringUtils.isBlank(message.getString("content")))
        {
            throw new ServiceException("AI 管家回答内容为空");
        }
        return message.getString("content");
    }
}
