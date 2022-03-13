package com.training.redditclone.controllers;

import com.training.redditclone.dto.ArticleRequest;
import com.training.redditclone.dto.ArticleResponse;
import com.training.redditclone.services.ArticleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("api/articles")
@AllArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity createArticle(@RequestBody ArticleRequest articleRequest) throws ParseException {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getCredentials());
        articleService.save(articleRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ArticleResponse getArticle(@PathVariable Long id) {
        return articleService.getArticle(id);
    }

    @GetMapping
    public List<ArticleResponse> getAllArticles(){
        return articleService.getAllArticles();
    }

    @GetMapping("/by-category/{id}")
    public List<ArticleResponse> getArticleByCategory(@PathVariable Long id){
        return articleService.getArticlesByCategory(id);
    }

    @GetMapping("/by-user/{name}")
    public List<ArticleResponse> getArticleByUsername(String username){
        return articleService.getAllArticlesByUsername(username);
    }
}
