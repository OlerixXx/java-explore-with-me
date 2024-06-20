package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HitDto {
    @NotBlank(message = "Поле приложения не может быть пустым.")
    @NotNull
    private String app;
    @NotBlank(message = "Поле ссылки не может быть пустым.")
    @NotNull
    private String uri;
    private String ip;
    private String timestamp;
}
