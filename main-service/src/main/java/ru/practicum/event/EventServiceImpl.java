package ru.practicum.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.HitDto;
import ru.practicum.StatisticsClient;
import ru.practicum.category.CategoryRepository;
import ru.practicum.category.model.Category;
import ru.practicum.event.dto.request.EventDto;
import ru.practicum.event.dto.response.EventFullDto;
import ru.practicum.event.dto.response.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.*;
import ru.practicum.exception.custom.ParticipationRequestLimitException;
import ru.practicum.exception.custom.PublishedEventUpdateException;
import ru.practicum.exception.custom.StatusRequestException;
import ru.practicum.request.ParticipationRequestRepository;
import ru.practicum.request.dto.request.ParticipationRequestDtoList;
import ru.practicum.request.dto.response.ParticipationRequestDto;
import ru.practicum.request.dto.response.ParticipationRequestDtoUpdate;
import ru.practicum.request.mapper.ParticipationRequestMapper;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    @PersistenceContext
    private final EntityManager entityManager;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final LocationRepository locationRepository;
    private final CategoryRepository categoryRepository;

    public EventFullDto create(EventDto eventDto, Long userId) {
        if (!userRepository.userExists(userId)) {
            throw new NoSuchElementException();
        }
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);

        locationRepository.save(eventDto.getLocation());
        Event event = EventMapper.toEvent(eventDto, user, categoryRepository.findById(eventDto.getCategory()).orElseThrow(NoSuchElementException::new));
        event.setState(State.PENDING);
        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    public EventFullDto update(EventDto eventDto, Long userId, Long eventId) {
        Event eventOld = eventRepository.findById(eventId).orElseThrow(NoSuchElementException::new);
        if (!userRepository.userExists(userId) || !Objects.equals(eventOld.getInitiator().getId(), userId)) {
            throw new NoSuchElementException();
        }
        if (eventOld.getState() == State.PUBLISHED) {
           throw new PublishedEventUpdateException("Only pending or canceled events can be changed") ;
        }
        if (eventDto.getLocation() != null) {
            locationRepository.save(eventDto.getLocation());
        }
        return EventMapper.toEventFullDto(eventRepository.save(EventMapper.toEventUpdate(eventOld, eventDto)));
    }

    public List<EventShortDto> getAllUserEvents(Long userId, Pageable makePage) {
        if (!userRepository.userExists(userId)) {
            throw new NoSuchElementException();
        }
        return EventMapper.toEventShortDtoList(eventRepository.findAllByInitiatorId(userId, makePage));
    }

    public EventFullDto getEvent(Long userId, Long eventId) {
        if (!userRepository.userExists(userId)) {
            throw new NoSuchElementException();
        }
        return EventMapper.toEventFullDto(eventRepository.findById(eventId).orElseThrow(NoSuchElementException::new));

    }

    public EventFullDto adminUpdate(EventDto eventDto, Long eventId) {
        Event eventOld = eventRepository.findById(eventId).orElseThrow(NoSuchElementException::new);
        if (eventOld.getState() == State.PUBLISHED || eventOld.getState() == State.CANCELED) {
            throw new PublishedEventUpdateException("Cannot publish the event because it's not in the right state: PUBLISHED");
        }
        if (eventDto.getLocation() != null) {
            locationRepository.save(eventDto.getLocation());
        }
        return EventMapper.toEventFullDto(eventRepository.save(EventMapper.toEventUpdate(eventOld, eventDto)));
    }

    public List<EventFullDto> adminSearch(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable makePage) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> event = cq.from(Event.class);

        Predicate userPredicate = !users.isEmpty() ? event.get("initiator").get("id").in(users) : cb.conjunction();
        Predicate statePredicate = !states.isEmpty() ? event.get("state").in(states) : cb.conjunction();
        Predicate categoriesPredicate = !categories.isEmpty() ? event.get("category").get("id").in(categories) : cb.conjunction();
        Predicate rangeStartPredicate = (rangeStart != null) ? cb.greaterThanOrEqualTo(event.get("eventDate"), rangeStart) : cb.greaterThanOrEqualTo(event.get("eventDate"), LocalDateTime.now());
        Predicate rangeEndPredicate = (rangeStart != null) ? cb.lessThanOrEqualTo(event.get("eventDate"), rangeEnd) : cb.conjunction();

        cq.where(
                cb.and(
                        userPredicate,
                        statePredicate,
                        categoriesPredicate,
                        rangeStartPredicate,
                        rangeEndPredicate
                )
        );

        TypedQuery<Event> typedQuery = entityManager.createQuery(cq);
        // Пагинация
        typedQuery.setFirstResult((int) makePage.getOffset());
        typedQuery.setMaxResults(makePage.getPageSize());
        List<Event> result = typedQuery.getResultList();
        return EventMapper.toEventFullDtoList(result);
    }

    public List<EventShortDto> search(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable makePage, Sorted sorted) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> event = cq.from(Event.class);

        Predicate annotationPredicate = StringUtils.hasText(text) ? cb.like(cb.lower(event.get("annotation")), "%" + text.toLowerCase() + "%") : cb.conjunction();
        Predicate descriptionPredicate = StringUtils.hasText(text) ? cb.like(cb.lower(event.get("description")), "%" + text.toLowerCase() + "%") : cb.conjunction();
        Predicate categoriesPredicate = !categories.isEmpty() ? event.get("category").get("id").in(categories) : cb.conjunction();
        Predicate paidPredicate = (paid != null) ? cb.equal(event.get("paid"), paid) : cb.conjunction();
        Predicate notNullEventDatePredicate = cb.isNotNull(event.get("eventDate"));
        Predicate rangeStartPredicate = (rangeStart != null) ? cb.greaterThanOrEqualTo(event.get("eventDate"), rangeStart) : cb.greaterThanOrEqualTo(event.get("eventDate"), LocalDateTime.now());
        Predicate rangeEndPredicate = (rangeEnd != null) ? cb.lessThanOrEqualTo(event.get("eventDate"), rangeEnd) : cb.conjunction();
        Predicate availablePredicate = onlyAvailable ? cb.lessThan(event.get("confirmed_requests"), event.get("participant_limit")) : cb.conjunction();

        cq.where(
                cb.and(
                        annotationPredicate,
                        descriptionPredicate,
                        categoriesPredicate,
                        paidPredicate,
                        rangeStartPredicate,
                        rangeEndPredicate,
                        notNullEventDatePredicate,
                        availablePredicate
                )
        );

        // Сортировка
        if (sorted != null) {
            switch (sorted) {
                case EVENT_DATE:
                    cq.orderBy(cb.asc(event.get("eventDate")));
                    break;
                case VIEWS:
                    cq.orderBy(cb.asc(event.get("views")));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown sorted parameter: " + sorted);
            }
        }
        TypedQuery<Event> typedQuery = entityManager.createQuery(cq);
        // Пагинация
        typedQuery.setFirstResult((int) makePage.getOffset());
        typedQuery.setMaxResults(makePage.getPageSize());
        List<Event> result = typedQuery.getResultList();
        return EventMapper.toEventShortDtoList(result);
    }

    @Transactional
    public EventFullDto getPublicEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(NoSuchElementException::new);
        if (event.getState().equals(State.PUBLISHED)) {
            return EventMapper.toEventFullDto(event);
        } else {
            throw new NoSuchElementException();
        }
    }

    public ParticipationRequestDtoUpdate updateRequest(ParticipationRequestDtoList requestDtoList, Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(NoSuchElementException::new);
        if (!userRepository.userExists(userId) || !Objects.equals(event.getInitiator().getId(), userId)) {
            throw new NoSuchElementException();
        }
        if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ParticipationRequestLimitException("The participant limit has been reached!");
        }

        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            return ParticipationRequestMapper.toParticipationRequestDtoUpdate(new ArrayList<>());
        } else {
            List<ParticipationRequest> requestList = participationRequestRepository.findAllById(requestDtoList.getRequestIds());
            requestList.forEach(request -> {
                if (request.getStatus().equals(Status.CONFIRMED) || request.getStatus().equals(Status.REJECTED)) {
                    throw new StatusRequestException("Статус должен быть только PENDING");
                }
                if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                    eventRepository.incrementRequests(eventId);
                    request.setStatus(Status.CONFIRMED);
                } else {
                    request.setStatus(Status.REJECTED);
                }
            });
            participationRequestRepository.saveAll(requestList);

            return ParticipationRequestMapper.toParticipationRequestDtoUpdate(requestList);
        }
    }

    public List<ParticipationRequestDto> getRequestsByEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(NoSuchElementException::new);
        if (!userRepository.userExists(userId) || !Objects.equals(event.getInitiator().getId(), userId)) {
            throw new NoSuchElementException();
        }
        return ParticipationRequestMapper.toParticipationRequestDtoList(participationRequestRepository.findAllByEventId(eventId));
    }

    private Event getEventWithViews() {
        return null;
    }
}
