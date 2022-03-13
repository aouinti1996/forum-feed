package com.training.redditclone.repositories;

import com.training.redditclone.entities.Article;
import com.training.redditclone.entities.Category;
import com.training.redditclone.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article,Long> {
    List<Article> findAllByCategory(Category category);

    List<Article> findByUser(User user);
}
