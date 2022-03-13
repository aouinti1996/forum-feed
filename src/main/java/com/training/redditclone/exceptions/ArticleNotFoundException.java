package com.training.redditclone.exceptions;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(String categoryName) {
        super(categoryName);
    }
}
