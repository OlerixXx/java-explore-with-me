package ru.practicum.event.dto.request;

import lombok.Data;
import ru.practicum.event.model.Location;
import ru.practicum.validated.Create;
import ru.practicum.validated.Update;
import ru.practicum.validator.FutureOrTwoHoursLater;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class EventDto {
    @Size(min = 20, max = 2000, groups = {Create.class, Update.class})
    @NotBlank(message = "Краткое описание не может быть пустым.", groups = Create.class)
    String annotation;
    @NotNull(groups = {Create.class})
    Long category;
    @Size(min = 20, max = 7000, groups = {Create.class, Update.class})
    @NotBlank(message = "Полное описание не может быть пустым.", groups = Create.class)
    String description;
    @NotNull(message = "Поле с датой не может быть пустым.", groups = Create.class)
    @FutureOrTwoHoursLater(message = "Дата и время события должны быть не раньше, чем через два часа от текущего момента", groups = {Create.class, Update.class})
    LocalDateTime eventDate;
    @NotNull(groups = {Create.class})
    Location location;
    Boolean paid;
    @Min(value = 0, groups = {Create.class, Update.class})
    Integer participantLimit;
    Boolean requestModeration;
    String stateAction;
    @Size(min = 3, max = 120, groups = {Create.class, Update.class})
    @NotBlank(message = "Заголовок не может быть пустым.", groups = Create.class)
    String title;
}
