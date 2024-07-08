package ru.practicum.category;

import org.springframework.data.domain.Pageable;
import ru.practicum.category.dto.request.CategoryDto;
import ru.practicum.category.model.Category;

import java.util.List;

public interface CategoryService {

    Category create(CategoryDto categoryDto);

    void delete(Long catId);

    Category update(CategoryDto categoryDto, Long catId);

    List<Category> getAll(Pageable makePage);

    Category getById(Long catId);
}
