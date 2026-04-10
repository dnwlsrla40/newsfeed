package com.newsfeed.demo.domain.news.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "news", uniqueConstraints = {
        @UniqueConstraint(name = "uk_news_url", columnNames = {"url"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // 뉴스 제목

    @Column(columnDefinition = "TEXT", nullable = false)
    private String url; // 원문 링크

    @Column(nullable = false)
    private String source; // 출처 (네이버 IT, 다음 경제 등)

    @Column(columnDefinition = "TEXT")
    private String content; // (옵션) 원문 본문 일부 혹은 전체

    @Column(columnDefinition = "TEXT")
    private String summary; // AI가 요약해줄 3줄 핵심 내용

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private NewsCategory category; // 분류 (주식, 경제, IT 등)

    @Column(name = "is_read")
    @Builder.Default
    private boolean isRead = false; // 읽음 처리 여부 (나중에 UI에서 유용함)

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 수집 및 저장 시간

    // 비즈니스 로직: AI 요약본 업데이트
    public void updateSummary(String summary, NewsCategory category) {
        this.summary = summary;
        this.category = category;
    }
}
