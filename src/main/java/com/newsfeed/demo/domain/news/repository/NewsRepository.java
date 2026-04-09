package com.newsfeed.demo.domain.news.repository;

import com.newsfeed.demo.domain.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByIsReadFalseOrderByCreatedAtDesc();

    List<News> findByCategory(String category);

    boolean existsByUrl(String url);
}
