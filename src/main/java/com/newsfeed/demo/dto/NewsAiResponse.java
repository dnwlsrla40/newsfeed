package com.newsfeed.demo.dto;

import com.newsfeed.demo.domain.news.entity.NewsCategory;

public record NewsAiResponse (
    String summary,
    NewsCategory category
) {}