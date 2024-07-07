package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.request.CompilationDto;
import ru.practicum.compilation.dto.response.CompilationShortDto;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    public List<CompilationShortDto> getAllCompilations(Boolean pinned, Pageable makePage) {
        if (pinned == null) {
            return CompilationMapper.toCompilationShortDtoList(compilationRepository.findAll(makePage).toList());
        } else {
            return CompilationMapper.toCompilationShortDtoList(compilationRepository.findAllByPinned(pinned, makePage));
        }
    }

    public CompilationShortDto getCompilationsById(Long compId) {
        return CompilationMapper.toCompilationShortDto(compilationRepository.findById(compId).orElseThrow(NoSuchFieldError::new));
    }

    @Transactional
    public CompilationShortDto create(CompilationDto compilationDto) {
        List<Event> eventList;
        if (compilationDto.getEvents() != null) {
            eventList = eventRepository.findAllById(compilationDto.getEvents());
        } else {
            eventList = List.of();
        }
        Compilation compilation = compilationRepository.save(CompilationMapper.toCompilation(compilationDto, eventList));
        return CompilationMapper.toCompilationShortDto(compilation);
    }

    @Transactional
    public void delete(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Transactional
    public CompilationShortDto update(CompilationDto compilationDto, Long compId) {
        List<Event> eventList;
        if (compilationDto.getEvents() != null) {
            eventList = eventRepository.findAllById(compilationDto.getEvents());
        } else {
            eventList = List.of();
        }
        Compilation updatedCompilation = compilationRepository.save(CompilationMapper.toCompilationUpdate(compilationDto, eventList, compId));
        return CompilationMapper.toCompilationShortDto(updatedCompilation);
    }
}
