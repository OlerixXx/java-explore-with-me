package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.request.CategoryDto;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public Category create(CategoryDto categoryDto) {
        return categoryRepository.save(CategoryMapper.toCategory(categoryDto));
    }

    @Transactional
    public void delete(Long catId) {
        categoryRepository.deleteById(catId);
    }

    @Transactional
    public Category update(CategoryDto categoryDto, Long catId) {
        return categoryRepository.save(CategoryMapper.toCategoryUpdate(categoryDto, catId));
    }

    public List<Category> getAll(Pageable makePage) {
        return categoryRepository.findAll(makePage).toList();
    }

    public Category getCategory(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(NoSuchElementException::new);
    }
}
