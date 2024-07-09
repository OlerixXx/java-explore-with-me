package ru.practicum.request.mapper;

import ru.practicum.event.model.Event;
import ru.practicum.event.model.Status;
import ru.practicum.request.dto.response.ParticipationRequestDto;
import ru.practicum.request.dto.response.ParticipationRequestDtoUpdate;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ParticipationRequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(
                participationRequest.getId(),
                participationRequest.getCreated(),
                participationRequest.getEvent().getId(),
                participationRequest.getRequester().getId(),
                participationRequest.getStatus()
        );
    }

    public static ParticipationRequest toParticipationRequest(User user, Event event, Status status) {
        return new ParticipationRequest(
                null,
                LocalDateTime.now(),
                event,
                user,
                status
        );
    }

    public static ParticipationRequestDtoUpdate toParticipationRequestDtoUpdate(List<ParticipationRequest> requestList) {
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        for (ParticipationRequest participationRequest : requestList) {
            if (participationRequest.getStatus() == Status.CONFIRMED) {
                confirmedRequests.add(toParticipationRequestDto(participationRequest));
            } else {
                rejectedRequests.add(toParticipationRequestDto(participationRequest));
            }
        }
        return new ParticipationRequestDtoUpdate(confirmedRequests, rejectedRequests);
    }

    public static List<ParticipationRequestDto> toParticipationRequestDtoList(List<ParticipationRequest> allByRequesterId) {
        return allByRequesterId.stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }
}
