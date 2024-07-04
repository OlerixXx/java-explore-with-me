package ru.practicum.event;

import org.springframework.data.domain.Pageable;
import ru.practicum.event.dto.request.EventDto;
import ru.practicum.event.dto.response.EventFullDto;
import ru.practicum.event.dto.response.EventShortDto;
import ru.practicum.event.model.Sorted;
import ru.practicum.event.model.State;
import ru.practicum.request.dto.request.ParticipationRequestDtoList;
import ru.practicum.request.dto.response.ParticipationRequestDto;
import ru.practicum.request.dto.response.ParticipationRequestDtoUpdate;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto create(EventDto eventDto, Long userId);

    EventFullDto update(EventDto eventDto, Long userId, Long eventId);

    List<EventShortDto> getAllUserEvents(Long userId, Pageable makePage);

    EventFullDto getEvent(Long userId, Long eventId);

    ParticipationRequestDtoUpdate updateRequest(ParticipationRequestDtoList requestDtoList, Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestsByEvent(Long userId, Long eventId);

    List<EventFullDto> adminSearch(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable makePage);

    EventFullDto adminUpdate(EventDto eventDto, Long eventId);

    List<EventShortDto> search(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable makePage, Sorted sorted);

    EventFullDto getPublicEvent(Long eventId);
}