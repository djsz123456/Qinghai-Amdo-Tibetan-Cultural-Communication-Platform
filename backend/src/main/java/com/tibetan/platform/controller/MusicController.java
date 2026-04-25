package com.tibetan.platform.controller;

import com.tibetan.platform.dto.ApiResponse;
import com.tibetan.platform.entity.Music;
import com.tibetan.platform.repository.MusicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/music")
@RequiredArgsConstructor
public class MusicController {

    private final MusicRepository musicRepository;

    @GetMapping
    public ApiResponse<List<Music>> list(@RequestParam(required = false) String category) {
        List<Music> list = category != null
                ? musicRepository.findByCategory(category)
                : musicRepository.findAll();
        return ApiResponse.ok(list);
    }

    @GetMapping("/{id}")
    public ApiResponse<Music> detail(@PathVariable Long id) {
        return musicRepository.findById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.error(404, "音乐不存在"));
    }
}
