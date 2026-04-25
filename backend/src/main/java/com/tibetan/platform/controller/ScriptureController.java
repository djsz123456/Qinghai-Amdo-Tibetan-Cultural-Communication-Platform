package com.tibetan.platform.controller;

import com.tibetan.platform.dto.ApiResponse;
import com.tibetan.platform.entity.Scripture;
import com.tibetan.platform.repository.ScriptureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scriptures")
@RequiredArgsConstructor
public class ScriptureController {

    private final ScriptureRepository scriptureRepository;

    @GetMapping
    public ApiResponse<List<Scripture>> list(@RequestParam(required = false) String name) {
        List<Scripture> list = name != null
                ? scriptureRepository.findByScriptureName(name)
                : scriptureRepository.findAllByOrderBySortOrder();
        return ApiResponse.ok(list);
    }

    @GetMapping("/{id}")
    public ApiResponse<Scripture> detail(@PathVariable Long id) {
        return scriptureRepository.findById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.error(404, "经文不存在"));
    }
}
