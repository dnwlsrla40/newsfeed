package com.newsfeed.demo.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.demo.dto.NewsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "raw-news", groupId = "news-group")
    public void consumeNews(String message) {
        try {
            NewsDto newsDto = objectMapper.readValue(message, NewsDto.class);
            log.info("#### Consumer가 뉴스 수신 성공: {}", newsDto.getTitle());
            processNews(newsDto);
        } catch (JsonProcessingException e) {
            log.error("메시지 파싱 중 에러 발생: {}", e.getMessage());
        }
    }

    /**
     * TODO
     * 임시로 로직이 들어갈 자리
     * 나중에 OpenAI API 연동 시 메서드 사용
     */
    private void processNews(NewsDto newsDto) {
    }
}
