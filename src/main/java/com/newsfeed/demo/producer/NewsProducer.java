package com.newsfeed.demo.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsfeed.demo.dto.NewsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendNews(NewsDto news) {
        try {
            String message = objectMapper.writeValueAsString(news);
            kafkaTemplate.send("raw-news", message);
            log.info("Kafka에 뉴스 전송 완료: {}", news.getTitle());
        } catch (JsonProcessingException e) {
            log.error("JSON 변환 에러: ", e);
        }
    }
}
