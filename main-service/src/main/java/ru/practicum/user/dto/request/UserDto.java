package ru.practicum.user.dto.request;

import lombok.Data;
import ru.practicum.validated.Create;
import ru.practicum.validated.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    @Size(min = 2, max = 250, groups = {Create.class, Update.class})
    @NotBlank(message = "Имя не может быть пустым.", groups = Create.class)
    private String name;
    @Size(min = 6, max = 254, groups = {Create.class, Update.class})
    @NotBlank(message = "Электронная почта не может быть пустой.", groups = Create.class)
    @Email(message = "Электронная почта не соответсвует формату.", groups = {Create.class, Update.class})
    private String email;
}
