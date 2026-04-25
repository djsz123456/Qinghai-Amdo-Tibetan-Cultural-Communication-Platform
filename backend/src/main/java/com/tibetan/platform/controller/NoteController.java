package com.tibetan.platform.controller;

import com.tibetan.platform.config.JwtUtil;
import com.tibetan.platform.dto.ApiResponse;
import com.tibetan.platform.entity.Note;
import com.tibetan.platform.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteRepository noteRepository;
    private final JwtUtil jwtUtil;

    private Long extractUserId(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtUtil.getUserIdFromToken(token);
    }

    @GetMapping
    public ApiResponse<List<Note>> list(@RequestHeader("Authorization") String auth) {
        Long userId = extractUserId(auth);
        return ApiResponse.ok(noteRepository.findByUserIdOrderByUpdatedAtDesc(userId));
    }

    @PostMapping
    public ApiResponse<Note> save(@RequestHeader("Authorization") String auth, @RequestBody Note note) {
        Long userId = extractUserId(auth);
        note.setUserId(userId);
        return ApiResponse.ok(noteRepository.save(note));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@RequestHeader("Authorization") String auth, @PathVariable Long id) {
        Long userId = extractUserId(auth);
        return noteRepository.findById(id)
                .filter(n -> n.getUserId().equals(userId))
                .map(n -> { noteRepository.delete(n); return ApiResponse.<Void>ok("删除成功", null); })
                .orElse(ApiResponse.error(404, "笔记不存在"));
    }
}
