package com.newsfeed.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsDto {
    private String title;
    private String url;
    private String source; // 예: 네이버, 다음 등
    private String collectedAt;
}
