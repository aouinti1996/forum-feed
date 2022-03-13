package com.training.redditclone.services;

import com.training.redditclone.dto.CategoryDto;
import com.training.redditclone.entities.Category;
import com.training.redditclone.exceptions.SpringRedditException;
import com.training.redditclone.mappers.CategoryMapper;
import com.training.redditclone.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryDto save(CategoryDto categoryDto){
        categoryDto.setCreatedDate(Instant.now());
        Category save = categoryRepository.save(categoryMapper.mapDtoToCategory(categoryDto));
        categoryDto.setId(save.getId());
        return categoryDto;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::mapCategoryToDto)
                .collect(Collectors.toList());
    }

    public CategoryDto getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("No Category found with id: " + id));
        return categoryMapper.mapCategoryToDto(category);
    }
}
