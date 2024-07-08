package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.convert.ConvertIntegerArray;
import ru.practicum.convert.ConvertPageable;
import ru.practicum.event.dto.request.EventDto;
import ru.practicum.event.dto.response.EventFullDto;
import ru.practicum.event.dto.response.EventShortDto;
import ru.practicum.event.model.Sorted;
import ru.practicum.event.model.State;
import ru.practicum.request.dto.request.ParticipationRequestDtoList;
import ru.practicum.request.dto.response.ParticipationRequestDto;
import ru.practicum.request.dto.response.ParticipationRequestDtoUpdate;
import ru.practicum.validated.Create;
import ru.practicum.validated.Update;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class EventController {
    private final EventService eventService;

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto create(@RequestBody @Validated(Create.class) EventDto eventDto, @PathVariable Long userId) {
        return eventService.create(eventDto, userId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto update(@RequestBody @Validated(Update.class) EventDto eventDto, @PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.update(eventDto, userId, eventId);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getAllFromUser(@PathVariable Long userId, @RequestParam(defaultValue = "0") Integer from, @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getAllByUserId(userId, ConvertPageable.toMakePage(from, size));
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getById(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getById(userId, eventId);
    }

    @GetMapping("/events")
    public List<EventShortDto> search(@RequestParam(required = false, defaultValue = "") String text,
                                      @RequestParam(required = false, defaultValue = "") Integer[] categories,
                                      @RequestParam(required = false) Boolean paid,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                      @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                      @RequestParam(required = false) Sorted sorted,
                                      @RequestParam(required = false, defaultValue = "0") Integer from,
                                      @RequestParam(required = false, defaultValue = "10") Integer size) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new IllegalArgumentException("Event must be published");
        }
        return eventService.search(text, ConvertIntegerArray.toLongList(categories), paid, rangeStart, rangeEnd, onlyAvailable, ConvertPageable.toMakePage(from, size), sorted);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getPublicEvent(@PathVariable Long eventId) {
        log.info("GET запрос к /events/{}", eventId);
        return eventService.getPublicEvent(eventId);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> adminSearch(@RequestParam(required = false, defaultValue = "") Integer[] users,
                                          @RequestParam(required = false, defaultValue = "") State[] states,
                                          @RequestParam(required = false, defaultValue = "") Integer[] categories,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                          @RequestParam(required = false, defaultValue = "0") Integer from,
                                          @RequestParam(required = false, defaultValue = "10") Integer size) {
        if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new IllegalArgumentException("Event must be published");
        }
        return eventService.adminSearch(ConvertIntegerArray.toLongList(users), State.toStateList(states), ConvertIntegerArray.toLongList(categories), rangeStart, rangeEnd, ConvertPageable.toMakePage(from, size));
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto adminUpdate(@RequestBody @Validated(Update.class) EventDto eventDto, @PathVariable Long eventId) {
        return eventService.adminUpdate(eventDto, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public ParticipationRequestDtoUpdate updateRequest(@RequestBody @Validated ParticipationRequestDtoList requestDtoList, @PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.updateRequest(requestDtoList, userId, eventId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return eventService.getRequestsByEvent(userId, eventId);
    }

}
