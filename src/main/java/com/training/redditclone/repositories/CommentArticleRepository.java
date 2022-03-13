package com.training.redditclone.repositories;

import com.training.redditclone.entities.Article;
import com.training.redditclone.entities.CommentArticle;
import com.training.redditclone.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentArticleRepository extends JpaRepository<CommentArticle,Long> {
    List<CommentArticle> findByArticle(Article article);

    List<CommentArticle> findAllByUser(User user);
}
