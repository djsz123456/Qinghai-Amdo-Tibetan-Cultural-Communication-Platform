package com.tibetan.platform.controller;

import com.tibetan.platform.config.JwtUtil;
import com.tibetan.platform.dto.ApiResponse;
import com.tibetan.platform.entity.Favorite;
import com.tibetan.platform.repository.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;
    private final JwtUtil jwtUtil;

    private Long extractUserId(String authHeader) {
        return jwtUtil.getUserIdFromToken(authHeader.replace("Bearer ", ""));
    }

    @GetMapping
    public ApiResponse<List<Favorite>> list(@RequestHeader("Authorization") String auth) {
        return ApiResponse.ok(favoriteRepository.findByUserIdOrderByCreatedAtDesc(extractUserId(auth)));
    }

    @PostMapping
    public ApiResponse<Favorite> add(@RequestHeader("Authorization") String auth, @RequestBody Favorite fav) {
        Long userId = extractUserId(auth);
        fav.setUserId(userId);
        if (favoriteRepository.findByUserIdAndItemTypeAndItemId(userId, fav.getItemType(), fav.getItemId()).isPresent()) {
            return ApiResponse.error(400, "已收藏");
        }
        return ApiResponse.ok(favoriteRepository.save(fav));
    }

    @DeleteMapping
    @Transactional
    public ApiResponse<Void> remove(@RequestHeader("Authorization") String auth,
                                     @RequestParam String itemType, @RequestParam Long itemId) {
        favoriteRepository.deleteByUserIdAndItemTypeAndItemId(extractUserId(auth), itemType, itemId);
        return ApiResponse.ok("取消收藏成功", null);
    }
}
