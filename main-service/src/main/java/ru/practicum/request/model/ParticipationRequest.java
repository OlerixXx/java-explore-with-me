package ru.practicum.request.model;

import lombok.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.Status;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participation_requests")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    Long id;
    @Column(name = "created")
    LocalDateTime created;
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    Event event;
    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    User requester;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    Status status;
}