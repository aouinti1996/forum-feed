package com.training.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleRequest {
    private Long articleId;
    private String categoryName;
    private String articleName;
    private String url;
    private String description;
}
