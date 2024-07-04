package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.request.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.convert.ConvertPageable;
import ru.practicum.validated.Create;
import ru.practicum.validated.Update;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@RequestBody @Validated(Create.class) CategoryDto categoryDto) {
        return categoryService.create(categoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long catId) {
        categoryService.delete(catId);
    }

    @PatchMapping("/admin/categories/{catId}")
    public Category update(@RequestBody @Validated(Update.class) CategoryDto categoryDto, @PathVariable Long catId) {
        return categoryService.update(categoryDto, catId);
    }

    @GetMapping("/categories")
    public List<Category> getAll(@RequestParam(required = false, defaultValue = "0") Integer from, @RequestParam(required = false, defaultValue = "10") Integer size) {
        return categoryService.getAll(ConvertPageable.toMakePage(from, size));
    }

    @GetMapping("/categories/{catId}")
    public Category getCategory(@PathVariable Long catId) {
        return categoryService.getCategory(catId);
    }
}
