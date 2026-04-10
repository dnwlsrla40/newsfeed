package com.newsfeed.demo.service.impl;

import com.newsfeed.demo.domain.news.entity.News;
import com.newsfeed.demo.domain.news.repository.NewsRepository;
import com.newsfeed.demo.dto.NewsAiResponse;
import com.newsfeed.demo.dto.NewsDto;
import com.newsfeed.demo.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final OllamaChatModel chatModel;

    @Transactional
    @Override
    public void saveNews(NewsDto newsDto) {
        // TODO : 중복 제거 try, catch로 예외 처리
        if(newsRepository.existsByUrl(newsDto.getUrl())) {
            log.info("#### 중복 기사 발견, 저장 건너뜀: {}", newsDto.getTitle());
            return;
        }

        var converter = new BeanOutputConverter<>(NewsAiResponse.class);

        // ollma AI 분석
        String prompt = String.format(
                "다음 뉴스 제목을 분석해서 요약과 카테고리를 정해줘.\n" +
                        "제목: %s\n\n" +
                        "**카테고리 분류 규칙:**\n" +
                        "- IT: 반도체, AI, 통신, 스마트폰, 소프트웨어 관련\n" +
                        "- 주식: 증시, 코스피, 상장, 배당, 투자 관련\n" +
                        "- 경제: 물가, 금리, 환율, 정부 정책, 일반 기업 소식 관련\n" +
                        "- 위 항목에 해당하지 않으면 '기타'로 분류할 것.\n\n" +
                        "응답 형식은 반드시 다음 JSON 형식을 지켜야 해: \n%s",
                newsDto.getTitle(),
                converter.getFormat()
        );

        String aiResponse = chatModel.call(prompt);
        NewsAiResponse analysis = converter.convert(aiResponse);

        try {
            News news = News.builder()
                    .title(newsDto.getTitle())
                    .url(newsDto.getUrl())
                    .source(newsDto.getSource())
                    .summary(analysis.summary())
                    .category(analysis.category())
                    .build();

            News savedNews = newsRepository.save(news);
            log.info("#### AI 분석 및 저장 완료: Category={}, Title={}", news.getCategory(), news.getTitle());
        } catch (Exception e) {
            log.error("#### AI 응답 파싱 실패! 원본 응답: {}", e.getMessage());
        }
    }
}
