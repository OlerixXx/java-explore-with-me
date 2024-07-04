package ru.practicum.category.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.request.CategoryDto;
import ru.practicum.category.model.Category;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {
    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(
                null,
                categoryDto.getName()
        );
    }

    public static Category toCategoryUpdate(CategoryDto categoryDto, Long catId) {
        return new Category(
                catId,
                categoryDto.getName()
        );
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getName()
        );
    }
}
