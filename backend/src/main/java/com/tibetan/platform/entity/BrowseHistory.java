package com.tibetan.platform.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "browse_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BrowseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "item_type", nullable = false, length = 20)
    private String itemType;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "item_name", length = 200)
    private String itemName;

    @Column(name = "visited_at")
    private LocalDateTime visitedAt;

    @PrePersist
    protected void onCreate() { visitedAt = LocalDateTime.now(); }
}
