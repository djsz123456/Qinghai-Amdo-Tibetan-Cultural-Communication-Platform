package com.tibetan.platform.repository;

import com.tibetan.platform.entity.Scripture;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScriptureRepository extends JpaRepository<Scripture, Long> {
    List<Scripture> findAllByOrderBySortOrder();
    List<Scripture> findByScriptureName(String scriptureName);
}
