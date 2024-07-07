package ru.practicum.compilation;

import org.springframework.data.domain.Pageable;
import ru.practicum.compilation.dto.request.CompilationDto;
import ru.practicum.compilation.dto.response.CompilationShortDto;

import java.util.List;

public interface CompilationService {

    List<CompilationShortDto> getAllCompilations(Boolean pinned, Pageable makePage);

    CompilationShortDto getCompilationsById(Long compId);

    CompilationShortDto create(CompilationDto compilationDto);

    void delete(Long compId);

    CompilationShortDto update(CompilationDto compilationDto, Long compId);
}
