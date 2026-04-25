package com.tibetan.platform.controller;

import com.tibetan.platform.dto.ApiResponse;
import com.tibetan.platform.entity.TourSpot;
import com.tibetan.platform.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spots")
@RequiredArgsConstructor
public class SpotController {

    private final SpotRepository spotRepository;

    @GetMapping
    public ApiResponse<List<TourSpot>> list(@RequestParam(required = false) String region) {
        List<TourSpot> spots = region != null
                ? spotRepository.findByRegionOrderBySortOrder(region)
                : spotRepository.findAllByOrderBySortOrder();
        return ApiResponse.ok(spots);
    }

    @GetMapping("/{id}")
    public ApiResponse<TourSpot> detail(@PathVariable Long id) {
        return spotRepository.findById(id)
                .map(ApiResponse::ok)
                .orElse(ApiResponse.error(404, "景点不存在"));
    }
}
