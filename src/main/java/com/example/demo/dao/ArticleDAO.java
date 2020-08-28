package com.example.demo.dao;

import java.util.Optional;

import com.example.demo.dto.Article;

import org.springframework.data.jpa.repository.JpaRepository;



public interface ArticleDAO extends JpaRepository<Article, Long> {
    
    Optional<Article> findByContent(Long id); 

}