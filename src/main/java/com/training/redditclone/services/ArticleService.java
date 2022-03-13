package com.training.redditclone.services;

import com.training.redditclone.dto.ArticleRequest;
import com.training.redditclone.dto.ArticleResponse;
import com.training.redditclone.dto.SmsRequest;
import com.training.redditclone.entities.Article;
import com.training.redditclone.entities.Category;
import com.training.redditclone.entities.NotificationEmail;
import com.training.redditclone.entities.User;
import com.training.redditclone.exceptions.ArticleNotFoundException;
import com.training.redditclone.exceptions.PostNotFoundException;
import com.training.redditclone.mappers.ArticleMapper;
import com.training.redditclone.repositories.ArticleRepository;
import com.training.redditclone.repositories.CategoryRepository;
import com.training.redditclone.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Transactional
public class ArticleService {

    private final String[] badwords = {"bitch","ass","asshole","cunt","dick","fuck","shit"};
    private final ArticleRepository articleRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final ArticleMapper articleMapper;
    private final SmsService smsService;
    private static final int maxAlerts = 5;

    @Scheduled(fixedDelay = 60000)
    public void badwordsFilter() {
        boolean hasBadWords = false;
        System.out.println("In method Bad Words Filter ....");
        List<Article> articles = articleRepository.findAll();
        for (Article article : articles) {
            List<String> description = new ArrayList<String>(Arrays.asList(article.getDescription().split(" ")));
            List<String> finalString = new ArrayList<String>();
            for (String word : description) {
                for (String badword : badwords) {
                    if (word.matches(".*" + badword + ".*")) {
                        hasBadWords = true;
                        word = "****";
                    }
                }
                finalString.add(word);
            }
            article.setDescription(String.join(" ", finalString));

            if (hasBadWords) {
                if (article.getUser().getNumberOfAlerts() < maxAlerts) {
                    String message =
                            mailContentBuilder.build(article.getUser().getUsername() + ", This email is a warning for you not meeting the appropriate behaviour inside the app. Your recent post had offensive words. A matter that we condemn in the strongest of possible terms. We hope this act never repeats itself otherwise we will be in the obligation of taking punitive measures against you");
                    sendWarningNotification(message, article.getUser());
                    article.getUser().setNumberOfAlerts(article.getUser().getNumberOfAlerts() + 1);
                    SmsRequest smsAlert = new SmsRequest(article.getUser().getPhoneNumber(),"article.getUser()" +
                            ".getUsername() + \", This sms is a warning for you not meeting the appropriate behaviour" +
                            " inside the app. Your recent post had offensive words. A matter that we condemn in the strongest of possible terms. We hope this act never repeats itself otherwise we will be in the obligation of taking punitive measures against you\"");
                    smsService.sendSms(smsAlert);
                    hasBadWords = false;
                } else {
                    article.getUser().setEnabled(false);
                }

            }
        }
    }

    private void sendWarningNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail("Behaviour Warning", user.getEmail(),
                message));
    }

    public Article save(ArticleRequest articleRequest) throws ParseException {
        Category category =categoryRepository.findByName(articleRequest.getCategoryName())
                .orElseThrow(()-> new ArticleNotFoundException(articleRequest.getCategoryName()));
        User currentUser = authService.getCurrentUser();

        return articleRepository.save(articleMapper.map(articleRequest, category, currentUser));
    }

    @Transactional(readOnly = true)
    public ArticleResponse getArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(()-> new PostNotFoundException(id.toString()));
        return articleMapper.mapToDto(article);
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getAllArticles() {
        return articleRepository.findAll()
                .stream()
                .map(articleMapper::mapToDto)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getArticlesByCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new PostNotFoundException(id.toString()));
        List<Article> articles = articleRepository.findAllByCategory(category);
        return articles.stream().map(articleMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<ArticleResponse> getAllArticlesByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));
        return articleRepository.findByUser(user)
                .stream().map(articleMapper::mapToDto)
                .collect(toList());
    }

    public Article updateArticle(Article article){
        return articleRepository.save(article);
    }

    public void deleteArticle(Long id){
        articleRepository.deleteById(id);
    }
}
