package ru.practicum.event.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.request.CategoryDto;
import ru.practicum.event.dto.request.LocationDto;
import ru.practicum.event.model.State;
import ru.practicum.user.dto.response.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    Long id;
    String annotation;
    CategoryDto category;
    Long confirmedRequests;
    LocalDateTime createdOn;
    String description;
    LocalDateTime eventDate;
    UserShortDto initiator;
    LocationDto location;
    boolean paid;
    Integer participantLimit;
    LocalDateTime publishedOn;
    boolean requestModeration;
    State state;
    String title;
    Long views;
}
