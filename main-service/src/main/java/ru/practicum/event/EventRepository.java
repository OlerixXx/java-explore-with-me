package ru.practicum.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM events WHERE id=?1)", nativeQuery = true)
    boolean eventExists(Long userId);

    List<Event> findAllByInitiatorId(Long userId, Pageable page);

    List<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetween(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable makePage);

    List<Event> findAllByAnnotationContainingAndDescriptionContainingAndCategoryIdInAndPaidAndEventDateBetween(String annotation, String description, List<Long> categories, boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd );

    @Query(value = "select e from Event e where (e.annotation like %?1% or e.description like %?2%) and e.category.id in ?3 and e.paid = ?4 and e.eventDate is not null and e.eventDate >= ?5 and e.eventDate <= ?6")
    List<Event> searchEventByFiltersAll(String annotation, String description, List<Long> categories, boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query(value = "select e from Event e where (e.annotation like %?1% or e.description like %?2%) and e.category.id in ?3 and e.paid = ?4 and e.eventDate >= ?5 and e.eventDate is not null and e.eventDate <= ?6 and e.confirmedRequests <= e.participantLimit")
    List<Event> searchEventByFiltersOnlyAvailable(String annotation, String description, List<Long> categories, boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query("select e.initiator.id from Event e where e.id = ?1")
    Long findInitiatorByEventId(Long eventId);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.views = e.views + 1 WHERE e.id = ?1")
    void incrementViews(Long eventId);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.confirmedRequests = e.confirmedRequests + 1 WHERE e.id = ?1")
    void incrementRequests(Long eventId);
}