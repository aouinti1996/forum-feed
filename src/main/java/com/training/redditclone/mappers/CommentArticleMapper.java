package com.training.redditclone.mappers;

import com.training.redditclone.dto.CommentArticleDto;
import com.training.redditclone.entities.Article;
import com.training.redditclone.entities.CommentArticle;
import com.training.redditclone.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentArticleMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentArticleDto.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "article", source = "article")
    CommentArticle map(CommentArticleDto commentArticleDto, Article article, User user);

    @Mapping(target = "articleId", expression = "java(commentArticle.getArticle().getArticleId())")
    @Mapping(target = "userName", expression = "java(commentArticle.getUser().getUsername())")
    CommentArticleDto mapToDto(CommentArticle commentArticle);
}
