package ru.practicum.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentFullDto;
import ru.practicum.comment.dto.CommentShortDto;
import ru.practicum.convert.ConvertPageable;
import ru.practicum.validated.Create;
import ru.practicum.validated.Update;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto create(@RequestBody @Validated(Create.class) CommentDto commentDto,
                                 @PathVariable Long userId,
                                 @PathVariable Long eventId) {
        return commentService.create(commentDto, userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto update(@RequestBody @Validated(Update.class) CommentDto commentDto,
                                 @PathVariable Long commentId,
                                 @PathVariable Long userId,
                                 @PathVariable Long eventId) {
        return commentService.update(commentDto, userId, eventId, commentId);
    }

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long commentId, @PathVariable Long userId) {
        commentService.delete(userId, commentId);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentShortDto> getAllEventComments(@PathVariable Long eventId,
                                                     @RequestParam(required = false, defaultValue = "0") Integer from,
                                                     @RequestParam(required = false, defaultValue = "10") Integer size) {
        return commentService.getAllEventComments(eventId, ConvertPageable.toMakePage(from, size));
    }

    @GetMapping("/comments/{commentId}")
    public CommentShortDto getById(@PathVariable Long commentId) {
        return commentService.getById(commentId);
    }

    @DeleteMapping("/admin/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adminDelete(@PathVariable Long commentId) {
        commentService.adminDelete(commentId);
    }
}
