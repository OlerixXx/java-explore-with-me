package ru.practicum.compilation;

import org.springframework.data.domain.Pageable;
import ru.practicum.compilation.dto.request.CompilationDto;
import ru.practicum.compilation.dto.response.CompilationShortDto;

import java.util.List;

public interface CompilationService {

    List<CompilationShortDto> getAll(Boolean pinned, Pageable makePage);

    CompilationShortDto getById(Long compId);

    CompilationShortDto create(CompilationDto compilationDto);

    void delete(Long compId);

    CompilationShortDto update(CompilationDto compilationDto, Long compId);
}
