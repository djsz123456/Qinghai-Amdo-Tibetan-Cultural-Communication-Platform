package com.tibetan.platform.repository;

import com.tibetan.platform.entity.TourSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SpotRepository extends JpaRepository<TourSpot, Long> {
    List<TourSpot> findByRegionOrderBySortOrder(String region);
    List<TourSpot> findAllByOrderBySortOrder();
}
