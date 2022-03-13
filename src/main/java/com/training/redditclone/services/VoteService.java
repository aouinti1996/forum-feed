package com.training.redditclone.services;

import com.training.redditclone.dto.VoteDto;
import com.training.redditclone.entities.Article;
import com.training.redditclone.entities.Vote;
import com.training.redditclone.exceptions.PostNotFoundException;
import com.training.redditclone.exceptions.SpringRedditException;
import com.training.redditclone.repositories.ArticleRepository;
import com.training.redditclone.repositories.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Optional;

import static com.training.redditclone.entities.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final ArticleRepository articleRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDto voteDto)  {
        Article article = articleRepository.findById(voteDto.getArticleId())
                .orElseThrow(() -> new PostNotFoundException("Article Not Found with ID - " + voteDto.getArticleId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByArticleAndUserOrderByVoteIdDesc(article, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDto.getVoteType())) {
            throw new SpringRedditException("You have already "
                    + voteDto.getVoteType() + "'d this article");
        }
        if (UPVOTE.equals(voteDto.getVoteType())) {
            article.setVoteCount(article.getVoteCount() + 1);
        } else {
            article.setVoteCount(article.getVoteCount() - 1);
        }
        voteRepository.save(mapToVote(voteDto, article));
        articleRepository.save(article);
    }

    private Vote mapToVote(VoteDto voteDto, Article article) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .article(article)
                .user(authService.getCurrentUser())
                .build();
    }
}
