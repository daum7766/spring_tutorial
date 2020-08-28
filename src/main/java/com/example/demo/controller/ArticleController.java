package com.example.demo.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.demo.dao.ArticleDAO;
import com.example.demo.dto.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = { "*" })
public class ArticleController {
    @Autowired
    ArticleDAO articleDAO;

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    @GetMapping(value = "/article/all")
    public Object GetAllArticle() {
        List<Article> article = articleDAO.findAll();
        return article;
    }


    @GetMapping(value = "/article/{id}")
    public Object GetArticle(@PathVariable("id") Long id) {
        System.out.println(id);
        Optional<Article> article = articleDAO.findById(id);
        System.out.println(article);

        return article;
    }

    @PostMapping(value="/article")
    public Object PostArticle(@RequestBody Article article) {
        article.setCreateDate(LocalDateTime.now());
        return articleDAO.save(article);
    }

    @PutMapping(value = "/article/{id}")
    public Object PutArticle(@PathVariable("id") Long id, @RequestBody Article newArticle) {

        Optional<Article> articles = articleDAO.findById(id);
        return articles.map(
            article -> {
                System.out.println(article);
                article.setTitle(newArticle.getTitle());
                article.setContent(newArticle.getContent());
                return articleDAO.save(article);
            }
        );
    }

    @DeleteMapping(value = "/article/{id}")
    public void DeleteArticle(@PathVariable("id") Long id) {
        articleDAO.deleteById(id);
    }
    
}