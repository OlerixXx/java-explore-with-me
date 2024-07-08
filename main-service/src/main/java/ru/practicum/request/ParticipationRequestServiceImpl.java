package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.event.model.Status;
import ru.practicum.exception.custom.ExecuteException;
import ru.practicum.request.dto.response.ParticipationRequestDto;
import ru.practicum.request.mapper.ParticipationRequestMapper;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(NoSuchElementException::new);
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        ParticipationRequest participationRequest;

        if (event.getState() != State.PUBLISHED || Objects.equals(event.getInitiator().getId(), userId) || (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit())) {
            throw new ExecuteException("Could not execute statement");
        }
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            eventRepository.incrementRequests(eventId);
            participationRequest = participationRequestRepository.save(ParticipationRequestMapper.toParticipationRequest(user, event, Status.CONFIRMED));
        } else {
            participationRequest = participationRequestRepository.save(ParticipationRequestMapper.toParticipationRequest(user, event, Status.PENDING));
        }
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequest);
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = participationRequestRepository.findById(requestId).orElseThrow(NoSuchElementException::new);
        if (!Objects.equals(request.getRequester().getId(), userId)) {
            throw new NoSuchElementException();
        }
        request.setStatus(Status.CANCELED);
        participationRequestRepository.cancelRequest(requestId);
        return ParticipationRequestMapper.toParticipationRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getAll(Long userId) {
        return ParticipationRequestMapper.toParticipationRequestDtoList(participationRequestRepository.findAllByRequesterId(userId));
    }
}
