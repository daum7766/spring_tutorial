package com.example.demo.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.demo.dao.ArticleDAO;
import com.example.demo.dao.UserDAO;
import com.example.demo.dto.Article;
import com.example.demo.dto.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    UserDAO userDao;

    private final Log logger = LogFactory.getLog(getClass());

    @GetMapping(value = "/article/all")
    public Object GetAllArticle() {
        List<Article> articles = articleDAO.findAll();
        return articles;
    }


    @GetMapping(value = "/article/{id}")
    public Object GetArticle(@PathVariable("id") Long id) {
        Optional<Article> article = articleDAO.findById(id);
        return article;
    }

    @PostMapping(value="/article")
    public Object PostArticle(@RequestBody Article article) {
        // 유저정보 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDao.findById(username).get();
        article.setCreateDate(LocalDateTime.now());
        article.setUser(user);
        return articleDAO.save(article);
    }

    @PutMapping(value = "/article/{id}")
    public Object PutArticle(@PathVariable("id") Long id, @RequestBody Article newArticle) {

        Optional<Article> articles = articleDAO.findById(id);
        if(!articles.isPresent()){
            return new ResponseEntity<>("없는 게시글", HttpStatus.BAD_REQUEST);
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!articles.get().getUser().equals(username)){
            return new ResponseEntity<>("게시글 작성자가 아님", HttpStatus.FORBIDDEN);
        }
        Article article = articles.get();
        article.setTitle(newArticle.getTitle());
        article.setContent(newArticle.getContent());
        articleDAO.save(article);
        return new ResponseEntity<>("수정완료", HttpStatus.OK);

    }

    @DeleteMapping(value = "/article/{id}")
    public Object DeleteArticle(@PathVariable("id") Long id) {
        Optional<Article> articles = articleDAO.findById(id);
        if(!articles.isPresent()){
            return new ResponseEntity<>("없는 게시글", HttpStatus.BAD_REQUEST);
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!articles.get().getUser().equals(username)){
            return new ResponseEntity<>("게시글 작성자가 아님", HttpStatus.FORBIDDEN);
        }
        articleDAO.deleteById(id);
        return new ResponseEntity<>("삭제완료", HttpStatus.OK);
    }
    
}