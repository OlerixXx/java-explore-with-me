package ru.practicum.comment.dto;

import lombok.Data;
import ru.practicum.validated.Create;
import ru.practicum.validated.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CommentDto {
    @Size(min = 1, max = 3000, groups = {Create.class, Update.class})
    @NotBlank(message = "Имя категории не может быть пустым", groups = {Create.class, Update.class})
    String text;
}