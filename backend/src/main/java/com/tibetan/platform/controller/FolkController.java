package com.tibetan.platform.controller;

import com.tibetan.platform.dto.ApiResponse;
import com.tibetan.platform.entity.FolkCulture;
import com.tibetan.platform.repository.FolkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/folk")
@RequiredArgsConstructor
public class FolkController {

    private final FolkRepository folkRepository;

    @GetMapping
    public ApiResponse<List<FolkCulture>> list() {
        return ApiResponse.ok(folkRepository.findAllByOrderBySortOrder());
    }

    @GetMapping("/{id}")
    public ApiResponse<FolkCulture> detail(@PathVariable Long id) {
        return folkRepository.findById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.error(404, "民俗内容不存在"));
    }
}
