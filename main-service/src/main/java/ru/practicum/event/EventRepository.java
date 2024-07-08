package ru.practicum.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM events WHERE id=:userId)", nativeQuery = true)
    boolean eventExists(Long userId);

    List<Event> findAllByInitiatorId(Long userId, Pageable page);

    @Query(value = "select e from Event e where (e.annotation like %:annotation% or e.description like %:description%) and e.category.id in :categories and e.paid = :paid and e.eventDate is not null and e.eventDate >= :rangeStart and e.eventDate <= :rangeEnd")
    List<Event> searchEventByFiltersAll(String annotation, String description, List<Long> categories, boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query(value = "select e from Event e where (e.annotation like %:annotation% or e.description like %:description%) and e.category.id in :categories and e.paid = :paid and e.eventDate >= :rangeStart and e.eventDate is not null and e.eventDate <= :rangeEnd and e.confirmedRequests <= e.participantLimit")
    List<Event> searchEventByFiltersOnlyAvailable(String annotation, String description, List<Long> categories, boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    @Query("select e.initiator.id from Event e where e.id = :eventId")
    Long findInitiatorByEventId(Long eventId);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.views = :hits WHERE e.id = :eventId")
    void incrementViews(Long eventId, Long hits);

    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.confirmedRequests = e.confirmedRequests + 1 WHERE e.id = :eventId")
    void incrementRequests(Long eventId);
}