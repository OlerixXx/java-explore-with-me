package ru.practicum.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.Category;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "annotation", nullable = false)
    String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @Column(name = "confirmed_requests")
    Long confirmedRequests;
    @Column(name = "created_on", nullable = false)
    LocalDateTime createdOn;
    @Column(name = "description", nullable = false)
    String description;
    @Column(name = "event_date", nullable = false)
    LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    User initiator;
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    Location location;
    @Column(name = "paid", nullable = false)
    boolean paid;
    @Column(name = "participant_limit", nullable = false)
    Integer participantLimit;
    @Column(name = "published_on")
    LocalDateTime publishedOn;
    @Column(name = "request_moderation", nullable = false)
    boolean requestModeration;
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    State state;
    @Column(name = "title", nullable = false)
    String title;
    @Column(name = "views")
    Long views;
}
