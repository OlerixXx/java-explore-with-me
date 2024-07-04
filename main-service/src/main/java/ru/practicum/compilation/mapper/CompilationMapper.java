package ru.practicum.compilation.mapper;

import ru.practicum.compilation.dto.request.CompilationDto;
import ru.practicum.compilation.dto.response.CompilationShortDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilation(CompilationDto compilationDto, List<Event> eventList) {
        return new Compilation(
                null,
                eventList,
                compilationDto.getPinned() == null ? false : compilationDto.getPinned(),
                compilationDto.getTitle()
        );
    }

    public static Compilation toCompilationUpdate(CompilationDto compilationDto, List<Event> eventList, Long id) {
        return new Compilation(
                id,
                eventList,
                compilationDto.getPinned() == null ? false : compilationDto.getPinned(),
                compilationDto.getTitle()
        );
    }

    public static CompilationShortDto toCompilationShortDto(Compilation compilation) {
        return new CompilationShortDto(
                EventMapper.toEventShortDtoList(compilation.getEventList()),
                compilation.getId(),
                compilation.isPinned(),
                compilation.getTitle()
        );
    }

    public static List<CompilationShortDto> toCompilationShortDtoList(List<Compilation> compilationList) {
        return compilationList.stream()
                .map(CompilationMapper::toCompilationShortDto)
                .collect(Collectors.toList());
    }
}
