package com.tibetan.platform.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scriptures")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Scripture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tibetan_text", nullable = false, columnDefinition = "TEXT")
    private String tibetanText;

    @Column(name = "chinese_translation", columnDefinition = "TEXT")
    private String chineseTranslation;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "scripture_name", length = 200)
    private String scriptureName;
}
