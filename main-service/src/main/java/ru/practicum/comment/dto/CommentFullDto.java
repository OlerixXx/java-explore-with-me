package ru.practicum.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentFullDto {
    Long id;
    Long event;
    Long commentator;
    String text;
}
