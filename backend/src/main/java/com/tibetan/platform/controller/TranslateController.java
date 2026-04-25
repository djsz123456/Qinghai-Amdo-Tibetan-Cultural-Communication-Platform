package com.tibetan.platform.controller;

import com.tibetan.platform.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/translate")
@RequiredArgsConstructor
public class TranslateController {

    @Value("${translate.api-url}")
    private String apiUrl;

    @PostMapping
    @SuppressWarnings("unchecked")
    public ApiResponse<Map<String, Object>> translate(@RequestBody Map<String, String> req) {
        String text = req.get("text");
        String from = req.get("from");
        String to = req.get("to");
        if (text == null || text.isBlank()) return ApiResponse.error(400, "翻译文本不能为空");

        String langpair = ("zh".equals(from) ? "zh-CN" : from) + "|" + ("zh".equals(to) ? "zh-CN" : to);
        String url = apiUrl + "?q=" + URLEncoder.encode(text, StandardCharsets.UTF_8) + "&langpair=" + langpair;

        try {
            RestTemplate rest = new RestTemplate();
            Map<String, Object> result = rest.getForObject(url, Map.class);
            return ApiResponse.ok(result);
        } catch (Exception e) {
            return ApiResponse.error(500, "翻译服务异常：" + e.getMessage());
        }
    }
}
