package com.newsfeed.demo.service.impl;

import com.newsfeed.demo.domain.news.entity.News;
import com.newsfeed.demo.domain.news.repository.NewsRepository;
import com.newsfeed.demo.dto.NewsDto;
import com.newsfeed.demo.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    @Transactional
    @Override
    public void saveNews(NewsDto newsDto) {
        // TODO :
        if(newsRepository.existsByUrl(newsDto.getUrl())) {
            log.info("#### 중복 기사 발견, 저장 건너뜀: {}", newsDto.getTitle());
            return;
        }

        News news = News.builder()
                .title(newsDto.getTitle())
                .url(newsDto.getUrl())
                .source(newsDto.getSource())
                .build();

        News savedNews = newsRepository.save(news);
        log.info("#### DB 저장 성공 : id={}, Title={}, url={}, source={}", savedNews.getId(), savedNews.getTitle(), savedNews.getUrl(), savedNews.getSource());

    }
}
