package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Status;
import ru.practicum.request.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByEventId(Long eventId);

    List<ParticipationRequest> findAllByRequesterId(Long userId);

    @Query(value = "SELECT COUNT(*) FROM participation_requests WHERE event_id = ?1 AND status = ?2", nativeQuery = true)
    Long getCountEventRequests(Long event_id, String status);
}