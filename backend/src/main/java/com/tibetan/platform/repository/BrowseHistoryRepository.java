package com.tibetan.platform.repository;

import com.tibetan.platform.entity.BrowseHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BrowseHistoryRepository extends JpaRepository<BrowseHistory, Long> {
    List<BrowseHistory> findByUserIdOrderByVisitedAtDesc(Long userId);
    void deleteByUserId(Long userId);
}
