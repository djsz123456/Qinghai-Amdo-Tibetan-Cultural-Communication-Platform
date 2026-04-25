package com.tibetan.platform.repository;

import com.tibetan.platform.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long> {
    List<Music> findByCategory(String category);
}
