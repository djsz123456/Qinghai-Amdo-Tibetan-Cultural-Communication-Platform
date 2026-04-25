package com.tibetan.platform.repository;

import com.tibetan.platform.entity.FolkCulture;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FolkRepository extends JpaRepository<FolkCulture, Long> {
    List<FolkCulture> findAllByOrderBySortOrder();
}
