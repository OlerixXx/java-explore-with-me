package ru.practicum.compilation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.dto.response.EventShortDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationShortDto {
    List<EventShortDto> events;
    Long id;
    boolean pinned;
    String title;
}