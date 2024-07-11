package ru.practicum.comment;

import org.springframework.data.domain.Pageable;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentShortDto;

import java.util.List;

public interface CommentService {
    CommentFullDto create(CommentDto commentDto, Long userId, Long eventId);

    CommentFullDto update(CommentDto commentDto, Long userId, Long eventId, Long commentId);

    void delete(Long userId, Long commentId);

    List<CommentShortDto> getAllEventComments(Long eventId, Pageable page);

    CommentShortDto getById(Long commentId);

    void adminDelete(Long commentId);
}
