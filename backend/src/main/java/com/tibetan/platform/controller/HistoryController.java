package com.tibetan.platform.controller;

import com.tibetan.platform.dto.ApiResponse;
import com.tibetan.platform.entity.BrowseHistory;
import com.tibetan.platform.repository.BrowseHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryController {

    private final BrowseHistoryRepository historyRepository;

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) auth.getCredentials();
    }

    @GetMapping
    public ApiResponse<List<BrowseHistory>> list() {
        return ApiResponse.ok(historyRepository.findByUserIdOrderByVisitedAtDesc(getCurrentUserId()));
    }

    @PostMapping
    public ApiResponse<BrowseHistory> add(@RequestBody BrowseHistory history) {
        history.setUserId(getCurrentUserId());
        return ApiResponse.ok(historyRepository.save(history));
    }

    @DeleteMapping
    @Transactional
    public ApiResponse<Void> clear() {
        historyRepository.deleteByUserId(getCurrentUserId());
        return ApiResponse.ok("浏览记录已清空", null);
    }
}
