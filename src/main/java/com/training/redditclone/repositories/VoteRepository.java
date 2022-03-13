package com.training.redditclone.repositories;

import com.training.redditclone.entities.Article;
import com.training.redditclone.entities.User;
import com.training.redditclone.entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
    Optional<Vote> findTopByArticleAndUserOrderByVoteIdDesc(Article article, User currentUser);
}
