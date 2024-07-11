package ru.practicum.comment.mapper;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.comment.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static Comment toComment(CommentDto commentDto, Event event, User commentator) {
        return new Comment(
                null,
                event,
                commentator,
                LocalDateTime.now(),
                commentDto.getText()
        );
    }

    public static CommentFullDto toCommentFullDto(Comment comment) {
        return new CommentFullDto(
                comment.getId(),
                comment.getEvent().getId(),
                comment.getCommentator().getId(),
                comment.getText()
        );
    }

    public static CommentShortDto toCommentShortDto(Comment comment) {
        return new CommentShortDto(
                comment.getEvent().getId(),
                comment.getCommentator().getId(),
                comment.getText()
        );
    }

    public static Comment toCommentUpdate(Comment comment, CommentDto commentDto) {
        return new Comment(
                comment.getId(),
                comment.getEvent(),
                comment.getCommentator(),
                LocalDateTime.now(),
                commentDto.getText()
        );
    }

    public static List<CommentShortDto> toCommentShortDtoList(List<Comment> allByEventId) {
        return allByEventId.stream()
                .map(CommentMapper::toCommentShortDto)
                .collect(Collectors.toList());
    }
}
