package ru.practicum.category.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.validated.Create;
import ru.practicum.validated.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    @Size(min = 1, max = 50, groups = {Create.class, Update.class})
    @NotBlank(message = "Имя категории не может быть пустым", groups = {Create.class, Update.class})
    String name;
}
