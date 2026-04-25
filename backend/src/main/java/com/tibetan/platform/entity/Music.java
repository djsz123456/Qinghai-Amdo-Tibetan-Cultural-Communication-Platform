package com.tibetan.platform.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "music")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 100)
    private String artist;

    @Column(length = 50)
    private String category;

    @Column(length = 10)
    private String duration;

    @Column(name = "cover_url", length = 500)
    private String coverUrl;

    @Column(name = "audio_url", length = 500)
    private String audioUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }
}
