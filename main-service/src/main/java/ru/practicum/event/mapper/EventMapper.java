package ru.practicum.event.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.request.EventDto;
import ru.practicum.event.dto.response.EventFullDto;
import ru.practicum.event.dto.response.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {
    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getAnnotation(),
                event.getCategory() == null ? null : CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                UserMapper.toUserShortDto(event.getInitiator()),
                LocationMapper.toLocationDto(event.getLocation()),
                event.isPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.isRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static Event toEvent(EventDto eventDto, User user, Category category) {
        return new Event(
                null,
                eventDto.getAnnotation(),
                category,
                0L,
                LocalDateTime.now(),
                eventDto.getDescription(),
                eventDto.getEventDate(),
                user,
                eventDto.getLocation(),
                eventDto.getPaid() == null ? false : eventDto.getPaid(),
                eventDto.getParticipantLimit() == null ? 0 : eventDto.getParticipantLimit(),
                null,
                eventDto.getRequestModeration() == null ? true : eventDto.getRequestModeration(),
                eventDto.getStateAction() == null ? null : State.toState(eventDto.getStateAction()),
                eventDto.getTitle(),
                0L
        );
    }

    public static Event toEventUpdate(Event event, EventDto eventDto) {
        return new Event(
                event.getId(),
                eventDto.getAnnotation() == null ? event.getAnnotation() : eventDto.getAnnotation(),
                event.getCategory() == null ? null : event.getCategory(),
                event.getConfirmedRequests() == null ? null : event.getConfirmedRequests(),
                event.getCreatedOn(),
                eventDto.getDescription() == null ? event.getDescription() : eventDto.getDescription(),
                eventDto.getEventDate() == null ? event.getEventDate() : eventDto.getEventDate(),
                event.getInitiator(),
                eventDto.getLocation() == null ? event.getLocation() : eventDto.getLocation(),
                eventDto.getPaid() == null ? event.isPaid() : eventDto.getPaid(),
                eventDto.getParticipantLimit() == null ? event.getParticipantLimit() : eventDto.getParticipantLimit(),
                event.getPublishedOn() == null && eventDto.getStateAction() != null ? (State.toState(eventDto.getStateAction()) == State.PUBLISHED ? LocalDateTime.now() : null) : event.getPublishedOn(),
                eventDto.getRequestModeration() == null ? event.isRequestModeration() : eventDto.getRequestModeration(),
                eventDto.getStateAction() == null ? event.getState() : State.toState(eventDto.getStateAction()),
                eventDto.getTitle() == null ? event.getTitle() : eventDto.getTitle(),
                event.getViews() == null ? null : event.getViews()
        );
    }

    private static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.isPaid(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static List<EventShortDto> toEventShortDtoList(List<Event> eventList) {
        return eventList.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    public static List<EventFullDto> toEventFullDtoList(List<Event> eventList) {
        return eventList.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }
}
