package com.training.redditclone.services;

import com.training.redditclone.dto.CommentArticleDto;
import com.training.redditclone.entities.Article;
import com.training.redditclone.entities.CommentArticle;
import com.training.redditclone.entities.NotificationEmail;
import com.training.redditclone.entities.User;
import com.training.redditclone.exceptions.PostNotFoundException;
import com.training.redditclone.mappers.CommentArticleMapper;
import com.training.redditclone.repositories.ArticleRepository;
import com.training.redditclone.repositories.CommentArticleRepository;
import com.training.redditclone.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentArticleService {

    private static final String POST_URL = "";

    private final ArticleRepository articleRepository;
    private final CommentArticleRepository commentArticleRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentArticleMapper commentMapper;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentArticleDto commentDto)  {
        Article article = articleRepository.findById(commentDto.getArticleId())
                .orElseThrow(()-> new PostNotFoundException(commentDto.getArticleId().toString()));
        CommentArticle commentArticle = commentMapper.map(commentDto, article,authService.getCurrentUser());
        commentArticleRepository.save(commentArticle);

        String message =
                mailContentBuilder.build(article.getUser().getUsername() + " posted a commentArticle on your article." + POST_URL);
        sendCommentNotification(message, article.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getUsername() + " Commented on your article", user.getEmail(),
                message));

    }

    public List<CommentArticleDto> getAllCommentsForPost(Long postId) {
        Article article = articleRepository.findById(postId).orElseThrow(()-> new PostNotFoundException(postId.toString()));
        return commentArticleRepository.findByArticle(article)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }

    public List<CommentArticleDto> getAllCommentsForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(()-> new UsernameNotFoundException(userName));
        return commentArticleRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }
}
