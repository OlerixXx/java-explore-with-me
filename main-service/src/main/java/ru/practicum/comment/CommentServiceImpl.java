package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.mapper.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.user.UserRepository;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentFullDto create(CommentDto commentDto, Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        Event event = eventRepository.findById(eventId).orElseThrow(NoSuchElementException::new);
        return CommentMapper.toCommentFullDto(commentRepository.save(CommentMapper.toComment(commentDto, event, user)));
    }

    @Override
    public CommentFullDto update(CommentDto commentDto, Long userId, Long eventId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NoSuchElementException::new);
        if (!userRepository.userExists(userId) || !eventRepository.eventExists(eventId) || !commentRepository.ownerMatchesUserId(userId, commentId)) {
            throw new NoSuchElementException();
        }
        return CommentMapper.toCommentFullDto(commentRepository.save(CommentMapper.toCommentUpdate(comment, commentDto)));
    }

    @Override
    public void delete(Long userId, Long commentId) {
        if (!commentRepository.ownerMatchesUserId(userId, commentId)) {
            throw new NoSuchElementException();
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentShortDto> getAllEventComments(Long eventId, Pageable page) {
        return CommentMapper.toCommentShortDtoList(commentRepository.findAllByEventId(eventId, page));
    }

    @Override
    public CommentShortDto getById(Long commentId) {
        return CommentMapper.toCommentShortDto(commentRepository.findById(commentId).orElseThrow(NoSuchElementException::new));
    }

    @Override
    public void adminDelete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}
