package com.training.redditclone.mappers;

import com.training.redditclone.dto.CategoryDto;
import com.training.redditclone.entities.Article;
import com.training.redditclone.entities.Category;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "numberOfArticles", expression = "java(mapArticles(category.getArticles()))")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    CategoryDto mapCategoryToDto(Category category);

    default Integer mapArticles(List<Article> numberOfArticles) {
        return numberOfArticles.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "articles", ignore = true)
    Category mapDtoToCategory(CategoryDto categoryDto);
}
