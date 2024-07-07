package ru.practicum.request.dto.request;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import ru.practicum.event.model.Status;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ParticipationRequestDtoList {
    @UniqueElements
    @NotNull
    List<Long> requestIds;
    @NotNull
    Status status;
}