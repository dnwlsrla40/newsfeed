package com.newsfeed.demo.service.impl;

import com.newsfeed.demo.dto.NewsDto;
import com.newsfeed.demo.producer.NewsProducer;
import com.newsfeed.demo.service.JsoupService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class JsoupServiceImpl implements JsoupService {
    private final NewsProducer newsProducer;

    // 아침 8시 스케줄링 전, 테스트를 위해 1분마다 실행되게 설정
    @Scheduled(fixedDelay = 60000)
    public void scrapeITNews() {
        String url = "https://news.naver.com/section/105"; // 네이버 IT 뉴스 예시

        try {
            Document doc = Jsoup.connect(url).get();

            Elements titles = doc.select(".sa_text_strong");

            for (Element element : titles) {
                NewsDto dto = NewsDto.builder()
                        .title(element.text())
                        .url(element.parent().attr("href"))
                        .source("Naver IT")
                        .collectedAt(LocalDateTime.now().toString())
                        .build();

                newsProducer.sendNews(dto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
