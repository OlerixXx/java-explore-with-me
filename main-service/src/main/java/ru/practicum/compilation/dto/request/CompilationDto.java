package ru.practicum.compilation.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import ru.practicum.validated.Create;
import ru.practicum.validated.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CompilationDto {
    @UniqueElements(groups = {Create.class, Update.class})
    List<Long> events;
    Boolean pinned;
    @Size(min = 1, max = 50, groups = {Create.class, Update.class})
    @NotBlank(message = "Заголовок не может быть пустым", groups = Create.class)
    String title;
}