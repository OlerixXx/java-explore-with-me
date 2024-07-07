package ru.practicum.request.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Status;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    Long id;
    LocalDateTime created;
    Long event;
    Long requester;
    Status status;
}